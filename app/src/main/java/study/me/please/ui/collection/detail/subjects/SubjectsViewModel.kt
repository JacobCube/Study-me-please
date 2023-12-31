package study.me.please.ui.collection.detail.subjects

import android.app.Activity
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import study.me.please.base.BaseViewModel
import study.me.please.base.GeneralClipBoard
import study.me.please.data.io.BaseResponse
import study.me.please.data.io.QuestionIO
import study.me.please.data.io.subjects.CategoryIO
import study.me.please.data.io.subjects.ParagraphIO
import study.me.please.data.io.subjects.SubjectIO
import study.me.please.ui.components.FactCardCategoryUseCase
import study.me.please.ui.components.collapsing_layout.CollapsingLayoutState
import javax.inject.Inject

/** Communication bridge between UI and DB */
@HiltViewModel
class SubjectsViewModel @Inject constructor(
    private val repository: SubjectsRepository,
    val clipBoard: GeneralClipBoard,
    private val questionGenerator: QuestionGenerator
): BaseViewModel(), FactCardCategoryUseCase {

    companion object {
        /** in case we failed updating collection with the new questions */
        const val FAILED_INSERT = "failed_insert"
    }

    /** List of all subjects related to a collection */
    private val _subjectsList = MutableStateFlow<List<SubjectIO>?>(null)

    /** List of all categories */
    private val _categories = MutableStateFlow<List<CategoryIO>?>(null)

    /** response from generating questions */
    private val _questionsGeneratingResponse = MutableSharedFlow<BaseResponse<List<QuestionIO>>>()

    /** Filter for current subjects */
    val filter = MutableStateFlow(SubjectsFilter())

    /** response from generating questions */
    val questionsGeneratingResponse = _questionsGeneratingResponse.asSharedFlow()

    /** List of all subjects related to a collection */
    val subjects = _subjectsList.combine(filter) { subjects, filter ->
        // TODO searching in subjects
        subjects
    }

    /** List of all categories */
    override val categories = _categories.asStateFlow()

    val collapsingLayoutState = CollapsingLayoutState()

    /** Generates questions and saves them right after that */
    fun generateQuestions(
        checkedSubjectUids: List<String>,
        activity: Activity,
        collectionUid: String
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val collection = repository.getCollection(collectionUid)
            val response = questionGenerator.generateQuestions(
                activity = activity,
                subjects = _subjectsList.value?.filter { checkedSubjectUids.contains(it.uid) }.orEmpty(),
                allSubject = _subjectsList.value.orEmpty(),
                categories = categories.value.orEmpty(),
                excludedList = repository.getAllQuestions(collectionUid = collectionUid)
                    ?.map { it.uid }
                    .orEmpty()
            )
            if(response.data?.isNotEmpty() == true && collection != null) {
                repository.insertQuestions(response.data)
                repository.updateCollection(collection.apply {
                    questionUidList.addAll(response.data.map { it.uid })
                })
            }
            _questionsGeneratingResponse.emit(response.copy(
                errorCode = if(collection == null) FAILED_INSERT else response.errorCode
            ))
        }
    }

    /** Makes a request to return subjects */
    fun requestSubjectsList(collectionUid: String) {
        viewModelScope.launch {
            _categories.value = repository.getAllCategories()
            _subjectsList.value = repository.getSubjectsByCollection(collectionUid) ?: listOf()
            _subjectsList.value?.forEach {
                it.paragraphs.forEach { paragraph ->
                    paragraph.localCategory = _categories.value?.find { category ->
                        category.uid == paragraph.categoryUid
                    }
                }
            }
        }
    }

    override fun requestAllCategories() {
        viewModelScope.launch {
            _categories.value = repository.getAllCategories()
        }
    }

    /** Makes a request for a subject deletion from the DB */
    fun deleteSubject(subjectUid: String) {
        viewModelScope.launch {
            _subjectsList.update { previousSubjects ->
                previousSubjects?.toMutableList()?.apply {
                    removeIf { it.uid == subjectUid }
                }
            }
            repository.deleteSubject(subjectUid)
        }
    }

    /** Adds new subject and creates, but doesn't create a DB record for it */
    fun addNewSubject(collectionUid: String, prefix: String) {
        viewModelScope.launch {
            _subjectsList.update { previousSubjects ->
                previousSubjects?.toMutableList()?.apply {
                    add(SubjectIO(collectionUid = collectionUid, name = "$prefix ${this.size.plus(1)}"))
                }
            }
        }
    }

    /** Updates specific subject */
    fun updateSubject(subject: SubjectIO) {
        viewModelScope.launch {
            _subjectsList.value?.apply {
                find { it.uid == subject.uid }?.updateTO(subject)
            }
            repository.updateSubject(subject)
        }
    }

    /** iterates into all possible depths */
    private fun iterateFurther(paragraph: ParagraphIO, newParagraph: ParagraphIO) {
        paragraph.paragraphs.forEach { iterationParagraph ->
            if(iterationParagraph.uid == newParagraph.uid) {
                iterationParagraph.updateTO(newParagraph)
            }
            iterateFurther(paragraph = iterationParagraph, newParagraph = newParagraph)
        }
    }

    /** Updates specific subject */
    fun updateParagraph(subject: SubjectIO, newParagraph: ParagraphIO) {
        viewModelScope.launch {
            subject.paragraphs.forEach { paragraph ->
                iterateFurther(paragraph, newParagraph)
            }

            _subjectsList.value?.apply {
                find { it.uid == subject.uid }?.updateTO(subject)
            }
            repository.updateSubject(subject)
        }
    }

    override fun requestAddNewCategory(name: String) {
        viewModelScope.launch {
            val category = CategoryIO(name = name)
            _categories.update { previousCategories ->
                previousCategories?.toMutableList()?.apply {
                    add(category)
                }
            }
            repository.insertCategory(category)
        }
    }
}