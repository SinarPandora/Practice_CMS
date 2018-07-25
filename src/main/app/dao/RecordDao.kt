package app.dao

import app.entity.Record
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface RecordDao {

    fun addRecord(record: Record): Int

    fun getRecordNumber(): Int

    fun getAll(): List<Record>
}
