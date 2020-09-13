package com.example.gnsspositionapp.usecase.list

import android.content.Context
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.usecase.BaseUseCase
import com.example.gnsspositionapp.usecase.measure.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import org.threeten.bp.LocalDate
import java.io.*
import java.nio.charset.Charset
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class SaveLocationUseCase @Inject constructor(
    @ApplicationContext private val context : Context,
    private val repository: LocationRepository
) : BaseUseCase<Unit,Unit>(Dispatchers.IO){

    companion object {
        private const val HEADER = "位置名称,日時,緯度,経度,精度,高さ"
    }

    override fun execute(parameters : Unit) = flow{

        emit(Result.Loading)

        val dir = context.getExternalFilesDir(null) ?: throw FileNotFoundException("Storage Directory is not found.")

        while(true){

            val locationInfo = repository.notSavedLocations.poll() ?: break

            val date = LocalDate.now()

            val file = File(dir,"${date}.csv")

            var hasHeader = true

            if(!file.exists()){
                file.createNewFile()
                hasHeader = false
            }

            BufferedWriter(OutputStreamWriter(FileOutputStream(file,true), Charset.forName("UTF-8")))
                .use{

                    if(!hasHeader){
                        it.write(HEADER)
                        it.newLine()
                    }

                    it.write(locationInfo.toCSVFormat())
                    it.newLine()
                }
        }
        emit(Result.Success(Unit))
    }
}