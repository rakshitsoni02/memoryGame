package in.co.accedo.colormemory.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import in.co.accedo.colormemory.injection.component.ApplicationComponent;
import in.co.accedo.colormemory.test.common.injection.module.ApplicationTestModule;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
