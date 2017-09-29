package me.yimu.doucalendar

import android.os.Environment
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.PatternFlattener
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import java.io.File


/**
 * Created by linwei on 2017/9/25.
 */

object Logger {
    init {
        val androidPrinter = AndroidPrinter()             // Printer that print the log using android.util.Log

        val filePrinter = FilePrinter.Builder(getLogFolderPath())      // Specify the path to save log file
                .fileNameGenerator(DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
                .backupStrategy(NeverBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
                .logFlattener(PatternFlattener("{d yyyy-MM-dd HH:mm:ss.SSS} {l}/{t}: {m}"))
                .build()
        val level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
        XLog.init(level, LogConfiguration.Builder().build(), androidPrinter, filePrinter)
    }

    fun getLogFolderPath(): String {
        val path = File(Environment.getExternalStorageDirectory().path, "xlog")
        if (!path.exists()) {
            path.mkdir()
        }
        return path.path
    }

    fun d(tag: String, msg: String) {
        XLog.tag(tag).d(msg, msg)
    }

}
