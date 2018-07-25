package app.service

import app.dao.RecordDao
import app.entity.Record
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RecordServiceImp: RecordService {

    @Autowired
    private lateinit var recordDao: RecordDao

    override fun addRecord(record: Record): Int {
        return recordDao.addRecord(record)
    }

    override fun numberOfRecords(): Int {
        return recordDao.getRecordNumber()
    }

    override fun getAllRecords(): List<Record> {
        return recordDao.getAll()
    }
}
