package app.service

import app.entity.Record

interface RecordService {

    fun addRecord(record: Record): Int

    fun numberOfRecords(): Int

    fun getAllRecords(): List<Record>
}
