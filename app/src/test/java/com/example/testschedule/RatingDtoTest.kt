package com.example.testschedule

import com.example.testschedule.data.remote.dto.account.rating.DisrespectfulOmissionDto
import com.example.testschedule.data.remote.dto.account.rating.RatingDto
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RatingDtoTest {
    @Test
    fun personalRatingResponseMapsToRatingModel() {
        val response = Gson().fromJson(
            """
            {
              "subjects": [
                {
                  "id": 1,
                  "name": "Mobile Development",
                  "abbrev": "MD",
                  "lessonTypes": [
                    {
                      "id": 4,
                      "abbrev": "LR",
                      "termHoursId": 77,
                      "lessons": [
                        {
                          "id": 101,
                          "controlPoint": "1",
                          "dateString": "01.09.2026",
                          "gradebookOmissions": 1,
                          "marks": [{"mark": 9, "taskNumber": 1}],
                          "subGroup": 1
                        },
                        {
                          "id": 102,
                          "controlPoint": "2",
                          "dateString": "10.09.2026",
                          "gradebookOmissions": 2,
                          "marks": [{"mark": 8, "taskNumber": null}],
                          "subGroup": 1
                        }
                      ]
                    }
                  ]
                },
                {
                  "id": 2,
                  "name": "Empty subject",
                  "abbrev": "EMPTY",
                  "lessonTypes": [
                    {
                      "id": 2,
                      "abbrev": "PZ",
                      "termHoursId": 88,
                      "lessons": []
                    }
                  ]
                }
              ],
              "deadlines": [
                {
                  "termHoursId": 77,
                  "labCount": 2,
                  "deadlines": [{"lessonId": 102, "taskNumber": 2}]
                }
              ],
              "percentageMarks": [
                {"discipline": "MD", "date": "05.09.2026", "number": 75}
              ]
            }
            """.trimIndent(),
            RatingDto::class.java
        )

        val model = response.toModel(
            listOf(
                DisrespectfulOmissionDto(
                    date = "2026-09-10",
                    subject = DisrespectfulOmissionDto.SubjectDto(
                        id = 1,
                        name = "Mobile Development",
                        abbrev = "MD"
                    ),
                    lessonTypeAbbrev = "LR",
                    hours = 2,
                    term = 5
                )
            )
        )
        val summary = model.points.getValue("all_points").subjects.getValue("MD")
        val deadlineInfo = requireNotNull(summary.deadlineInfo)

        assertEquals(setOf("1", "2"), model.allPoints)
        assertEquals(listOf(9, 8), summary.listOfMarks)
        assertEquals(2, summary.countOfOmissions)
        assertEquals(
            listOf(0, 2),
            summary.types.getValue("LR").all.map { it.omissions }
        )
        assertEquals(2, deadlineInfo.totalLabs)
        assertEquals(1, deadlineInfo.submittedLabs)
        assertEquals("10.09.2026", deadlineInfo.deadlines.single().date)
        assertEquals(2, deadlineInfo.deadlines.single().taskNumber)
        assertEquals("75", summary.percentageMarks.single().number)
        assertEquals(
            setOf("PZ"),
            model.points.getValue("all_points").subjects.getValue("EMPTY").allTypes
        )
        assertNull(model.points.getValue("1").subjects.getValue("MD").deadlineInfo)
        assertEquals(
            emptyList<Any>(),
            model.points.getValue("1").subjects.getValue("MD").percentageMarks
        )
    }

    @Test
    fun disrespectfulOmissionIsNotDuplicatedAcrossSubgroups() {
        val response = Gson().fromJson(
            """
            {
              "subjects": [{
                "id": 20094,
                "name": "Databases",
                "abbrev": "DB",
                "lessonTypes": [{
                  "id": 4,
                  "abbrev": "LR",
                  "termHoursId": 1,
                  "lessons": [
                    {
                      "id": 1,
                      "controlPoint": "1",
                      "dateString": "04.09.2026",
                      "gradebookOmissions": 2,
                      "marks": [],
                      "subGroup": 1
                    },
                    {
                      "id": 2,
                      "controlPoint": "1",
                      "dateString": "04.09.2026",
                      "gradebookOmissions": 2,
                      "marks": [],
                      "subGroup": 2
                    }
                  ]
                }]
              }],
              "deadlines": [],
              "percentageMarks": []
            }
            """.trimIndent(),
            RatingDto::class.java
        )

        val model = response.toModel(
            listOf(
                DisrespectfulOmissionDto(
                    date = "2026-09-04",
                    subject = DisrespectfulOmissionDto.SubjectDto(
                        id = 20094,
                        name = "Databases",
                        abbrev = "DB"
                    ),
                    lessonTypeAbbrev = "LR",
                    hours = 2,
                    term = 5
                )
            )
        )
        val subject = model.points.getValue("all_points").subjects.getValue("DB")

        assertEquals(2, subject.countOfOmissions)
        assertEquals(1, subject.types.getValue("LR").all.count { it.omissions > 0 })
    }
}
