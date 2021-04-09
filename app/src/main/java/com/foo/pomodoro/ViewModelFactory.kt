//package com.foo.pomodoro
//
//import android.annotation.SuppressLint
//import android.app.Application
//import androidx.annotation.VisibleForTesting
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.foo.pomodoro.data.PomodoroRepository
//import com.foo.pomodoro.viewmodels.NewPomodoroViewModel
//
//class ViewModelFactory (
//    private val pomodoroRepository: PomodoroRepository
//) : ViewModelProvider.NewInstanceFactory() {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>) =
//        with(modelClass) {
//            when {
//                isAssignableFrom(NewPomodoroViewModel::class.java) ->
//                    NewPomodoroViewModel(pomodoroRepository)
//
//                else ->
//                    throw IllegalAccessException("Unknown ViewModel class: ${modelClass.name}")
//            }
//        } as T
//
//    companion object {
//
//    @SuppressLint("StaticFieldLeak")
//    @Volatile private var INSTANCE: ViewModelFactory? = null
//
//    fun getInstance(application: Application) =
//        INSTANCE ?: synchronized(ViewModelFactory::class.java) {
//            INSTANCE ?: ViewModelFactory(
//                Injection.provideTasksRepository(application.applicationContext))
//                .also { INSTANCE = it }
//        }
//
//
//    @VisibleForTesting
//    fun destroyInstance() {
//        INSTANCE = null
//    }
//}
//    }
//
//
//
//}