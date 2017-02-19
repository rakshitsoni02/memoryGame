package in.co.accedo.colormemory.injection.component;

import dagger.Subcomponent;
import in.co.accedo.colormemory.injection.PerActivity;
import in.co.accedo.colormemory.injection.module.ActivityModule;
import in.co.accedo.colormemory.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
