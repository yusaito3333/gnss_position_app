package com.example.gnsspositionapp.usecase.measure

import com.example.gnsspositionapp.data.Result
import android.content.Context
import com.example.gnsspositionapp.data.LocationInfo
import com.example.gnsspositionapp.usecase.BaseUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.io.*
import java.nio.charset.Charset
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayDeque

@Suppress("BlockingMethodInNonBlockingContext")
class SaveLocationUseCase @Inject constructor(
    @ApplicationContext private val context : Context
) : BaseUseCase<Queue<LocationInfo>,Unit>(Dispatchers.IO){

    override fun execute(parameters : Queue<LocationInfo>) = flow{

        emit(Result.Loading)

        val dir = context.getExternalFilesDir(null) ?: throw FileNotFoundException("Storage Directory is not found.")

        while(true){

            val locationInfo = parameters.poll() ?: break

            val date = LocalDate.now()

            val file = File(dir,"${date}.csv")

            if(!file.exists()){
                file.createNewFile()
            }

            BufferedWriter(OutputStreamWriter(FileOutputStream(file,true), Charset.forName("UTF-8")))
                .use{
                    it.write(locationInfo.toCSVFormat())
                }
        }
        emit(Result.Success(Unit))
    }
}