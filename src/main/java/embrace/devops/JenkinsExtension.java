package embrace.devops;

import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;


@Xml(prefix="jenkins")
@Extension(name="Jenkins")
@Configurations(JenkinsConfiguration.class)
public class JenkinsExtension {
	

}
