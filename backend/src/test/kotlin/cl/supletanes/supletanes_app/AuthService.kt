package cl.supletanes.supletanes_app.service

import androidx.compose.ui.semantics.password
import cl.supletanes.supletanes_app.model.User
import cl.supletanes.supletanes_app.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

// Clase simple para implementar la lógica de login y poder probarla.
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    fun login(email: String, pass: String): String? {
        val user = userRepository.findByEmail(email)
        return if (user != null && passwordEncoder.matches(pass, user.password)) {
            "token-jwt-falso-y-exitoso" // Login correcto
        } else {
            null // Login fallido
        }
    }
}

// --- Inicio del Test ---
class AuthServiceTest : BehaviorSpec({

    // Mocks para las dependencias
    val userRepository: UserRepository = mockk()
    val passwordEncoder: BCryptPasswordEncoder = mockk()

    // El servicio que vamos a probar
    val authService = AuthServiceImpl(userRepository, passwordEncoder)

    // Datos de prueba consistentes
    val testEmail = "test@example.com"
    val testPassword = "password123"
    val hashedPassword = "hash_secreto_guardado_en_db"
    val testUser = User(id = 1, email = testEmail, password = hashedPassword)

    // Limpiar mocks después de cada test
    afterTest {
        clearMocks(userRepository, passwordEncoder)
    }

    // --- Escenarios ---
    Given("un usuario intenta iniciar sesión") {

        // --- Escenario 1: Éxito ---
        // USAREMOS EL 'When' CORRECTO DE KOTEST
        com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.meta.When("el email existe y la contraseña es correcta") {
            // Preparamos los mocks para este caso:
            every { userRepository.findByEmail(testEmail) } returns testUser
            every { passwordEncoder.matches(testPassword, hashedPassword) } returns true

            // Ejecutamos la acción
            val result = authService.login(testEmail, testPassword)

            // Verificamos el resultado
            Then("el inicio de sesión debe ser exitoso y devolver un token") {
                result shouldNotBe null
            }
        }

        // --- Escenario 2: Contraseña incorrecta ---
        // USAREMOS EL 'When' CORRECTO DE KOTEST
        com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.meta.When("el email existe pero la contraseña es incorrecta") {
            // Preparamos los mocks para este caso:
            every { userRepository.findByEmail(testEmail) } returns testUser
            every { passwordEncoder.matches("contraseña-incorrecta", hashedPassword) } returns false

            // Ejecutamos la acción
            val result = authService.login(testEmail, "contraseña-incorrecta")

            // Verificamos el resultado
            Then("el inicio de sesión debe fallar y devolver null") {
                result shouldBe null
            }
        }

        // --- Escenario 3: Email no existe ---
        // USAREMOS EL 'When' CORRECTO DE KOTEST
        com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.meta.When("el email no existe en la base de datos") {
            // Preparamos el mock para este caso:
            every { userRepository.findByEmail("no-existe@example.com") } returns null

            // Ejecutamos la acción
            val result = authService.login("no-existe@example.com", testPassword)

            // Verificamos el resultado
            Then("el inicio de sesión debe fallar y devolver null") {
                result shouldBe null
            }
        }
    }
})
