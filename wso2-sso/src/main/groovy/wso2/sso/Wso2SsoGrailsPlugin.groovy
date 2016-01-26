package wso2.sso

import grails.plugins.*
import org.springframework.boot.context.embedded.FilterRegistrationBean
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean
import org.springframework.boot.context.embedded.ServletRegistrationBean
import org.springframework.core.Ordered
import org.wso2.carbon.identity.sso.agent.bean.SSOAgentConfig
import org.wso2.carbon.identity.sso.agent.saml.SSOAgentHttpSessionListener
import org.wso2.carbon.identity.sso.agent.saml.SSOAgentX509KeyStoreCredential
import org.wso2.sample.is.sso.agent.ForwardingServlet
import org.wso2.sample.is.sso.agent.SSOAgentSampleFilter
import org.wso2.sample.is.sso.agent.SampleContextEventListener


class Wso2SsoGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.11 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Wso2 Sso" // Headline display name of the plugin
    def author = "Faizal Hussein"
    def authorEmail = "faizal.hussein@b2b.com.my"
    def description = '''\
Brief summary/description of the plugin.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/wso2-sso"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    Closure doWithSpring() { {->
            // TODO Implement runtime spring config (optional)

        //register the servlet from the wso2 example
            forwardServlet (ServletRegistrationBean,
                            new ForwardingServlet(),
                            "/samlsso",
                            "/openid",
                            "/token",
                            "/logout") {
                loadOnStartup = 1
            }
        //register the filter from the wso2 example
            ssoAgentFilter (FilterRegistrationBean) {
                filter = bean(SSOAgentSampleFilter)
                urlPatterns = ['/*.gsp','/samlsso','/openid','/token','/logout']
                order = Ordered.HIGHEST_PRECEDENCE
            }
        }
        // register the seesionlistener
            ssoAgentSessionListener(ServletListenerRegistrationBean) {
                listener = bean(SSOAgentHttpSessionListener)
            }
        // register the contextevent listener
        ssoAgentContextEventListener(ServletListenerRegistrationBean) {
            listener = bean(SampleContextEventListener)
        }

    }

    void doWithDynamicMethods() {
        // TODO Implement registering dynamic methods to classes (optional)
    }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
