package com.example.supletanes.notifications

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bouncycastle.crypto.params.Blake3Parameters.context
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class NotificacionLlegaCorrectamenteTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `cuando RecordatorioCal se ejecuta entonces debe retornar success`() = runTest {
        val worker = TestListenableWorkerBuilder<RecordatorioCal>(context).build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
    }
}