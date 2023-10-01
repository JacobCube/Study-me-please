package study.me.please.ui.collection.detail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import study.me.please.data.io.CollectionIO
import study.me.please.data.io.QuestionIO
import study.me.please.data.io.SessionIO
import study.me.please.data.room.CollectionDao
import study.me.please.data.room.QuestionDao
import study.me.please.data.room.SessionDao
import javax.inject.Inject

/** Proxy for calling network end points */
class CollectionDetailRepository @Inject constructor(
    private val collectionDao: CollectionDao,
    private val sessionDao: SessionDao,
    private val questionDao: QuestionDao
) {

    /** Returns a collection by its uid - [collectionUid] */
    suspend fun getCollectionByUid(collectionUid: String): CollectionIO? {
        return withContext(Dispatchers.IO) {
            collectionDao.getCollectionByUid(collectionUid)
        }
    }

    /** Returns a list of questions by their identifiers - [questionUidList] */
    suspend fun getQuestionsByUid(questionUidList: List<String>): List<QuestionIO>? {
        return withContext(Dispatchers.IO) {
            questionDao.getQuestionsByUid(questionUidList)
        }
    }

    /** removes all questions with uid from the provided list [uidList] */
    suspend fun deleteQuestions(uidList: List<String>) {
        withContext(Dispatchers.IO) {
            questionDao.deleteQuestions(uidList)
        }
    }

    /** saves a collection */
    suspend fun saveCollection(collection: CollectionIO) {
        return withContext(Dispatchers.IO) {
            collectionDao.insertCollection(collection)
        }
    }

    /** saves a question */
    suspend fun saveQuestion(question: QuestionIO) {
        return withContext(Dispatchers.IO) {
            questionDao.insertQuestion(question)
        }
    }

    /** Get list of all sessions */
    suspend fun getSessions(): List<SessionIO>? {
        return withContext(Dispatchers.IO) {
            sessionDao.getAllSessions()
        }
    }

    /** removes all sessions */
    suspend fun saveSessions(sessions: List<SessionIO>) {
        withContext(Dispatchers.IO) {
            sessionDao.insertSessions(sessions)
        }
    }
}