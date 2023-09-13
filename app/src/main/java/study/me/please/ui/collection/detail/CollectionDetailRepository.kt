package study.me.please.ui.collection.detail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import study.me.please.data.io.CollectionIO
import study.me.please.data.room.CollectionDao
import javax.inject.Inject

/** Proxy for calling network end points */
class CollectionDetailRepository @Inject constructor(
    private val collectionDao: CollectionDao
) {

    /** Returns a collection by its uid - [collectionUid] */
    suspend fun getCollectionByUid(collectionUid: String): CollectionIO? {
        return withContext(Dispatchers.IO) {
            collectionDao.getCollectionByUid(collectionUid)
        }
    }

    /** removes all questions with uid from the provided list [uidList] */
    suspend fun deleteQuestions(uidList: List<String>) {
        withContext(Dispatchers.IO) {
            collectionDao.deleteQuestions(uidList)
        }
    }

    /** removes all answers with uid from the provided list [uidList] */
    suspend fun deleteAnswers(uidList: List<String>) {
        withContext(Dispatchers.IO) {
            collectionDao.deleteAnswers(uidList)
        }
    }

    /** saves current collection detail */
    suspend fun saveDetail(collection: CollectionIO) {
        return withContext(Dispatchers.IO) {
            collectionDao.insertCollection(collection)
        }
    }
}