package com.github.britter.springbootherokudemo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotEmpty

@Repository
interface RecordRepository : JpaRepository<Record, Long>

@Entity
class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0
    @NotEmpty
    var data: String? = null

}
