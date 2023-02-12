package dinf.html.pages

import dinf.html.templates.Layout
import dinf.plugins.FORM_LOGIN_EMAIL_FIELD
import dinf.plugins.FORM_LOGIN_PASSWORD_FIELD
import dinf.routes.LoginResource
import kotlinx.html.FormMethod
import kotlinx.html.emailInput
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.label
import kotlinx.html.passwordInput
import kotlinx.html.submitInput

class LoginPage(private val resource: LoginResource) : Page {

    override fun Layout.apply() {
        content {
            h2 {
                +"Login"
            }
            if (isFailedAuth()) {
                h3 {
                    +"Email or password were incorrect"
                }
            }
            form(action = loginURL, method = FormMethod.post) {
                label {
                    +"Email"
                    emailInput(name = FORM_LOGIN_EMAIL_FIELD) {
                        placeholder = "gendalf@tatooine.rick"
                        required = true
                        if (isFailedAuth()) {
                            attributes["aria-invalid"] = "true"
                        }
                    }
                }

                label {
                    +"Password"
                    passwordInput(name = FORM_LOGIN_PASSWORD_FIELD) {
                        required = true
                        if (isFailedAuth()) {
                            attributes["aria-invalid"] = "true"
                        }
                    }
                }

                submitInput {
                    value = "Sign In"
                }
            }
        }
    }

    private fun isFailedAuth(): Boolean {
        return resource.failed ?: false
    }

}
