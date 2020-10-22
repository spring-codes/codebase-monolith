package com.cheroliv.codebase.monolith.security

import java.lang.reflect.InvocationTargetException
import java.sql.SQLException
import javax.servlet.Servlet
import javax.servlet.ServletContext
import kotlin.jvm.Throws

object H2ConfigurationHelper {
    /**
     *
     * createServer.
     *
     * @param port a [String] object.
     * @return a [Object] object.
     * @throws SQLException if any.
     */
    /**
     *
     * createServer.
     *
     * @return a [Object] object.
     * @throws java.sql.SQLException if any.
     */
    @JvmOverloads
    @Throws(SQLException::class)
    fun createServer(port: String = "9092"): Any {
        return try {
            val loader = Thread.currentThread().contextClassLoader
            val serverClass = Class.forName("org.h2.tools.Server", true, loader)
            val createServer = serverClass.getMethod("createTcpServer", Array<String>::class.java)
            createServer.invoke(null, *arrayOf<Any>(arrayOf("-tcp", "-tcpAllowOthers", "-tcpPort", port)))
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("Failed to load and initialize org.h2.tools.Server", e)
        } catch (e: LinkageError) {
            throw RuntimeException("Failed to load and initialize org.h2.tools.Server", e)
        } catch (e: SecurityException) {
            throw RuntimeException("Failed to get method org.h2.tools.Server.createTcpServer()", e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Failed to get method org.h2.tools.Server.createTcpServer()", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Failed to invoke org.h2.tools.Server.createTcpServer()", e)
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("Failed to invoke org.h2.tools.Server.createTcpServer()", e)
        } catch (e: InvocationTargetException) {
            val t = e.targetException
            if (t is SQLException) {
                throw t
            }
            throw RuntimeException("Unchecked exception in org.h2.tools.Server.createTcpServer()", t)
        }
    }

    /**
     *
     * initH2Console.
     *
     * @param servletContext a [javax.servlet.ServletContext] object.
     */
    fun initH2Console(servletContext: ServletContext) {
        try {
            // We don't want to include H2 when we are packaging for the "prod" profile and won't
            // actually need it, so we have to load / invoke things at runtime through reflection.
            val loader = Thread.currentThread().contextClassLoader
            val servletClass = Class.forName("org.h2.server.web.WebServlet", true, loader)
            val servlet = servletClass.getDeclaredConstructor().newInstance() as Servlet
            val h2ConsoleServlet = servletContext.addServlet("H2Console", servlet)
            h2ConsoleServlet.addMapping("/h2-console/*")
            h2ConsoleServlet.setInitParameter("-properties", "src/main/resources/")
            h2ConsoleServlet.setLoadOnStartup(1)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("Failed to load and initialize org.h2.server.web.WebServlet", e)
        } catch (e: LinkageError) {
            throw RuntimeException("Failed to load and initialize org.h2.server.web.WebServlet", e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Failed to load and initialize org.h2.server.web.WebServlet", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Failed to load and initialize org.h2.server.web.WebServlet", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Failed to instantiate org.h2.server.web.WebServlet", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Failed to instantiate org.h2.server.web.WebServlet", e)
        }
    }
}