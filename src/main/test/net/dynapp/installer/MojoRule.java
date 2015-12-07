package net.dynapp.installer;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;

/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Written By: brandon on 7/12/15
 */
public class MojoRule extends AbstractMojoTestCase implements TestRule {
	public Statement apply(final Statement statement, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				MojoRule.this.setUp();
				try {
					statement.evaluate();
				} finally {
					MojoRule.this.tearDown();
				}
			}
		};
	}

	@Override
	public Mojo lookupMojo(String goal, File pom) throws Exception {
		return super.lookupMojo(goal, pom);
	}
}
