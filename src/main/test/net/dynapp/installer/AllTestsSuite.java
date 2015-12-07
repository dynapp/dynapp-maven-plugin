package net.dynapp.installer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		DalvikCompileMojoTest.class,
		MavenInstallTests.class
})
public final class AllTestsSuite {}