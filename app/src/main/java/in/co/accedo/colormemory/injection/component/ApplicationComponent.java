package in.co.accedo.colormemory.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import in.co.accedo.colormemory.data.DataManager;
import in.co.accedo.colormemory.data.SyncService;
import in.co.accedo.colormemory.data.local.DatabaseHelper;
import in.co.accedo.colormemory.data.local.PreferencesHelper;
import in.co.accedo.colormemory.data.remote.RibotsService;
import in.co.accedo.colormemory.injection.ApplicationContext;
import in.co.accedo.colormemory.injection.module.ApplicationModule;
import in.co.accedo.colormemory.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    RibotsService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}
