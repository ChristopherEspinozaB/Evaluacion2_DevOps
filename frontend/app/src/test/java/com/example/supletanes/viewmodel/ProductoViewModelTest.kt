package com.example.supletanes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.supletanes.data.model.Producto
import com.example.supletanes.data.repository.ProductoRepository
import com.example.supletanes.ui.screens.products.viewmodel.ProductoViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: ProductoRepository
    private lateinit var viewModel: ProductoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk<ProductoRepository>(relaxed = true) // Usar 'mockk<Tipo>()' es más claro

        // Configurar el mock para la llamada inicial de init {}
        coEvery { repository.obtenerProductos() } returns Result.success(emptyList())

        // Pasar el repositorio MOCK al constructor del ViewModel
        viewModel = ProductoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `cuando se inserta producto exitosamente entonces debe cargar productos`() = runTest {
        // Arrange
        val producto = Producto(
            id = 0L, // Se ignora al crear
            nombre = "Proteína Whey",
            descripcion = "Suplemento proteico",
            precio = 45990.0,
            stock = 100,
            categoria = "Proteínas"
        )

        val productoCreado = producto.copy(id = 1L)
        val listaActualizada = listOf(productoCreado)

        coEvery { repository.crearProducto(any()) } returns Result.success(productoCreado)
        coEvery { repository.obtenerProductos() } returns Result.success(listaActualizada)

        // Act
        viewModel.insert(producto)
        advanceUntilIdle()

        // Assert
        assertEquals(listaActualizada, viewModel.allProducts.value)
        assertEquals("Producto creado exitosamente", viewModel.mensajeSnackbar.value)
        assertEquals(false, viewModel.isLoading.value)

        coVerify(exactly = 1) { repository.crearProducto(any()) }
        coVerify(atLeast = 1) { repository.obtenerProductos() }
    }

    @Test
    fun `cuando falla insertar producto entonces debe mostrar mensaje de error`() = runTest {
        // Arrange
        val producto = Producto(
            id = 0L,
            nombre = "Creatina",
            descripcion = "Monohidrato",
            precio = 25990.0,
            stock = 50,
            categoria = "Creatinas"
        )
        val errorMsg = "Error de conexión"

        coEvery { repository.crearProducto(any()) } returns Result.failure(Exception(errorMsg))

        // Act
        viewModel.insert(producto)
        advanceUntilIdle()

        // Assert
        assertEquals("Error al crear producto", viewModel.mensajeSnackbar.value)
        assertEquals(false, viewModel.isLoading.value)

        coVerify(exactly = 1) { repository.crearProducto(any()) }
    }

    @Test
    fun `cuando se elimina producto exitosamente entonces debe recargar lista`() = runTest {
        // Arrange
        val productoId = 1L
        val listaActualizada = emptyList<Producto>()

        coEvery { repository.eliminarProducto(productoId) } returns Result.success(Unit)
        coEvery { repository.obtenerProductos() } returns Result.success(listaActualizada)

        // Act
        viewModel.delete(productoId)
        advanceUntilIdle()

        // Assert
        assertEquals(listaActualizada, viewModel.allProducts.value)
        assertEquals(false, viewModel.isLoading.value)

        coVerify(exactly = 1) { repository.eliminarProducto(productoId) }
        coVerify(atLeast = 1) { repository.obtenerProductos() }
    }

    @Test
    fun `cuando se actualiza producto exitosamente entonces debe recargar lista`() = runTest {
        // Arrange
        val producto = Producto(
            id = 1L,
            nombre = "BCAA Actualizado",
            descripcion = "Nueva fórmula",
            precio = 29990.0,
            stock = 75,
            categoria = "Aminoácidos"
        )
        val listaActualizada = listOf(producto)

        coEvery { repository.actualizarProducto(any(), any()) } returns Result.success(producto)
        coEvery { repository.obtenerProductos() } returns Result.success(listaActualizada)

        // Act
        viewModel.update(producto)
        advanceUntilIdle()

        // Assert
        assertEquals(listaActualizada, viewModel.allProducts.value)

        coVerify(exactly = 1) { repository.actualizarProducto(1L, any()) }
        coVerify(atLeast = 1) { repository.obtenerProductos() }
    }

    @Test
    fun `cuando hay timeout al crear producto entonces debe mostrar mensaje especifico`() = runTest {
        // Arrange
        val producto = Producto(
            id = 0L,
            nombre = "Pre-Entreno",
            descripcion = "Energía",
            precio = 35990.0,
            stock = 60,
            categoria = "Pre-Entreno"
        )

        coEvery { repository.crearProducto(any()) } returns
                Result.failure(java.net.SocketTimeoutException("timeout"))

        // Act
        viewModel.insert(producto)
        advanceUntilIdle()

        // Assert
        assertEquals(
            "Timeout: El servidor está despertando. Intenta de nuevo en 30 segundos",
            viewModel.mensajeSnackbar.value
        )
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `limpiarMensaje debe setear mensajeSnackbar a null`() = runTest {
        // Arrange
        // Mockear la respuesta de crearProducto para que devuelva un Producto
        val productoMock = Producto(1L, "Test", null, 10.0, 1, "Test")
        coEvery { repository.crearProducto(any()) } returns Result.success(productoMock)
        // Mockear obtenerProductos para que el flujo de recarga funcione (si se activa)
        coEvery { repository.obtenerProductos() } returns Result.success(listOf(productoMock))

        // Act - establecer un mensaje primero (usando la inserción que ahora está mockeada)
        viewModel.insert(Producto(0L, "Test", null, 10.0, 1, "Test"))
        advanceUntilIdle()

        // Verificar que hay un mensaje (de éxito)
        assertEquals("Producto creado exitosamente", viewModel.mensajeSnackbar.value)
        assertNotNull(viewModel.mensajeSnackbar.value)

        // Act
        viewModel.limpiarMensaje()

        // Assert
        assertNull(viewModel.mensajeSnackbar.value)

        // Limpiar el mock si es necesario para otros tests
        clearMocks(repository)
    }

    @Test
    fun `refrescar debe llamar a cargarProductos`() = runTest {
        // Arrange
        val productosActualizados = listOf(
            Producto(1L, "Producto 1", "Desc 1", 10.0, 5, "Cat 1"),
            Producto(2L, "Producto 2", "Desc 2", 20.0, 10, "Cat 2")
        )

        coEvery { repository.obtenerProductos() } returns Result.success(productosActualizados)

        // Act
        viewModel.refrescar()
        advanceUntilIdle()

        // Assert
        assertEquals(productosActualizados, viewModel.allProducts.value)

        // Se llama al menos 2 veces: 1 en init{} y 1 en refrescar()
        coVerify(atLeast = 2) { repository.obtenerProductos() }
    }
}