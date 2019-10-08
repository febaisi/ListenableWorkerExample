# ListenableWorkerExample

### Simple project using ListenableWorkers with Kotlin.

Google provides good documentation for it - but it's always helpful to have an example to follow. I had a couple of issues to find
a piece of code to follow for the ListenableWorker implementation with Kotlin, which should be widely used since this is the right way
to retrieve Locations using WorkManager. 

Even though it's a simple project I tried to use the MVVM pattern, so it also covers a little bit of:
- Basic kodein -> Kotlin dependency injection
- ModelViews
- ViewModelProviders
- LiveData
- Repository (In a fake way)
- Location Permission (Including new Android 10 - Background location)


---

The location data is being requested by a OneTimeWorkRequest object and handled using ListenableWork. 
The results are displayed on the Activity through liveData (MVVM).


<img src="https://i.makeagif.com/media/10-07-2019/KiYF4M.gif" alt="ListenableWorker" width="270" height="555">
