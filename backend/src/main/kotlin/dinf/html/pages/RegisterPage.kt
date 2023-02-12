package dinf.html.pages

import dinf.html.templates.Layout
import dinf.plugins.FORM_LOGIN_EMAIL_FIELD
import dinf.plugins.FORM_LOGIN_PASSWORD_FIELD
import dinf.routes.RegisterResource
import kotlinx.html.FormMethod
import kotlinx.html.a
import kotlinx.html.emailInput
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.label
import kotlinx.html.passwordInput
import kotlinx.html.small
import kotlinx.html.submitInput

class RegisterPage(private val resource: RegisterResource) : Page {

    override fun Layout.apply() {
        content {
            h2 {
                +"Register"
            }
            form(action = registerURL, method = FormMethod.post) {
                label {
                    +"Email"
                    emailInput(name = FORM_LOGIN_EMAIL_FIELD) {
                        placeholder = "gendalf@tatooine.rick"
                        required = true
                        if (userExists()) {
                            attributes["aria-invalid"] = "true"
                            small {
                                +"Such email already exists. Try another email or "
                                a(href = loginURL) {
                                    +"login"
                                }
                            }
                        }
                    }
                }

                label {
                    +"Password"
                    passwordInput(name = FORM_LOGIN_PASSWORD_FIELD) {
                        required = true
                    }
                }

                submitInput {
                    value = "Sign Up"
                }
            }
        }
    }

    private fun userExists(): Boolean {
        return resource.userExists ?: false
    }

}
