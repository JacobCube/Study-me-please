package study.me.please.data.io

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName
import com.squadris.squadris.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import study.me.please.data.room.AppRoomDatabase
import java.io.Serializable
import java.util.UUID

/** Fact containing specific related 2 information sets */
@Entity(tableName = AppRoomDatabase.ROOM_FACT_TABLE)
data class FactIO(

    /** unique identifier */
    @PrimaryKey
    val uid: String = UUID.randomUUID().toString(),

    /** short content, that is a key to [longInformation], it works as a prompt in most of the cases */
    @SerializedName("short_key_information")
    var shortKeyInformation: String = "",

    /** longer content, that could possibly explaing the [shortKeyInformation] */
    @SerializedName("long_information")
    var longInformation: String = "",

    /** List of information */
    var textList: List<String> = listOf(),

    /** Image which can be questioned as well as answered with */
    @SerializedName("prompt_mage")
    var promptImage: LargePathAsset? = null,

    /** type of this fact */
    var type: FactType = FactType.DEFINITION,

    /** date of creation of this data object */
    @SerializedName("date_created")
    @ColumnInfo("date_created")
    val dateCreated: Long = DateUtils.now.timeInMillis,

    /** nested facts within this fact */
    var nestedFacts: List<FactIO> = listOf()
): Serializable {

    /** Whether there is not visible data */
    @get:Ignore
    @get:Exclude
    val isEmpty: Boolean
        get() = shortKeyInformation.isBlank()
                && longInformation.isBlank()
                && promptImage?.isEmpty != false
                && (textList.isEmpty() || type != FactType.LIST)

    /** Whether this data can be taken seriously */
    suspend fun isSeriousDataPoint() = withContext(Dispatchers.Default) {
        (hasTextData() || promptImage?.isEmpty == false)
                && shortKeyInformation.isNotBlank()
    }

    /** Whether this data can be taken seriously */
    suspend fun hasTextData() = withContext(Dispatchers.Default) {
        (textList.any { it.isNotBlank() } || longInformation.isNotBlank())
                && shortKeyInformation.isNotBlank()
    }

    /** Updates this object with new data */
    fun updateTO(fact: FactIO) {
        this.shortKeyInformation = fact.shortKeyInformation
        this.type = fact.type
        this.longInformation = fact.longInformation
        this.nestedFacts = fact.nestedFacts
        this.textList = fact.textList
        this.promptImage = fact.promptImage
    }

    @Ignore
    @Exclude
    override fun toString(): String {
        return "{" +
                "uid: $uid," +
                "shortKeyInformation: $shortKeyInformation," +
                "longInformation: $longInformation," +
                "nestedFacts: $nestedFacts," +
                "textList: $textList," +
                "promptImage: $promptImage," +
                "type: $type," +
                "dateCreated: $dateCreated" +
                "}"
    }

    @Ignore
    @Exclude
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FactIO

        if (uid != other.uid) return false
        if (shortKeyInformation != other.shortKeyInformation) return false
        if (longInformation != other.longInformation) return false
        if (textList != other.textList) return false
        if (promptImage != other.promptImage) return false
        if (type != other.type) return false
        if (dateCreated != other.dateCreated) return false
        if (nestedFacts != other.nestedFacts) return false

        return true
    }

    @Ignore
    @Exclude
    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + shortKeyInformation.hashCode()
        result = 31 * result + longInformation.hashCode()
        result = 31 * result + textList.hashCode()
        result = 31 * result + (promptImage?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + dateCreated.hashCode()
        result = 31 * result + nestedFacts.hashCode()
        return result
    }
}