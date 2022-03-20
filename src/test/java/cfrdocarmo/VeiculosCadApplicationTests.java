package cfrdocarmo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cfrdocarmo.util.DatabaseCleaner;
import cfrdocarmo.util.ResourceUtils;
import cfrdocarmo.veiculo.domain.model.MarcaFabricante;
import cfrdocarmo.veiculo.domain.model.Veiculo;
import cfrdocarmo.veiculo.domain.model.exception.EntidadeNaoEncontradaException;
import cfrdocarmo.veiculo.domain.model.repository.VeiculoRepository;
import cfrdocarmo.veiculo.domain.model.service.CadastroVeiculoService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class VeiculosCadApplicationTests {
	
	private static final String VEICULO_ID = "/{veiculoId}";

	private static final String POR_MARCA_OU_ANO = "/porMarcaOuAno";

	private static final Long VEICULO_ID_INEXISTENTE = 100L;

	private Veiculo jetta;
	private Veiculo novoVeiculo2;
	private int quantidadeVeiculosCadastrados;
	private String jsonCorretoVeiculo;
	private String jsonVeiculoParcial;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private CadastroVeiculoService cadastroVeiculo;
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/veiculos";
		
		RestAssured.requestSpecification = new RequestSpecBuilder().
                setContentType(ContentType.JSON).
                build();
		
//		RestAssured.responseSpecification = new ResponseSpecBuilder().
//                expectContentType(ContentType.JSON).
//                build();
		
		jsonCorretoVeiculo = ResourceUtils.getContentFromResource(
				"/json/correto/veiculo.json");
		
		jsonVeiculoParcial = ResourceUtils.getContentFromResource(
				"/json/correto/veiculo.json");
		
		databaseCleaner.clearTables();
		prepararDados();
	}
	
	private void prepararDados() {
		jetta = new Veiculo();
		jetta.setVeiculo("Jetta");
		jetta.setAno(2019);
		jetta.setDescricao("Execução de Testes ");
		jetta.setMarca(MarcaFabricante.VOLKSWAGEN);
		
		novoVeiculo2 = new Veiculo();
		novoVeiculo2.setVeiculo("Uno");
		novoVeiculo2.setAno(2021);
		novoVeiculo2.setDescricao("Massa de Dados");
		novoVeiculo2.setMarca(MarcaFabricante.FIAT);
		
		cadastroVeiculo.salvar(jetta);
		cadastroVeiculo.salvar(novoVeiculo2);
		
		quantidadeVeiculosCadastrados = (int) veiculoRepository.count();
	}

	@Test
	public void deveAtribuirId_QuandoCadastrarVeiculoComDadosCorretos() {
		Veiculo novoVeiculo = new Veiculo();
		novoVeiculo.setVeiculo("Jetta");
		novoVeiculo.setAno(2019);
		novoVeiculo.setDescricao("Execução de Testes");
		novoVeiculo.setMarca(MarcaFabricante.VOLKSWAGEN);
		
		novoVeiculo = cadastroVeiculo.salvar(novoVeiculo);
		
		assertThat(novoVeiculo).isNotNull();
	}
	
	@Test()
	public void deveFalhar_QuandoCadastrarVeiculoSemNome() {
		Veiculo novoVeiculo = new Veiculo();
		novoVeiculo.setVeiculo(null);
		
		DataIntegrityViolationException erroEsperado =
				Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
					cadastroVeiculo.salvar(novoVeiculo);
				});
		assertThat(erroEsperado).isNotNull();
	}
	
	@Test()
	public void deveFalhar_QuandoExcluirVeiculoInexistente() {
		EntidadeNaoEncontradaException erroEsperado =
				Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
					cadastroVeiculo.excluir(VEICULO_ID_INEXISTENTE);
				});
		assertThat(erroEsperado).isNotNull();
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarVeiculos() {
		given()
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorreto_QuandoConsultarVeiculoExistente() {
		given()
			.pathParam("veiculoId", jetta.getId())
		.when()
			.get(VEICULO_ID)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatus404_QuandoConsultarVeiculoInexistente() {
		given()
			.pathParam("veiculoId", VEICULO_ID_INEXISTENTE)
		.when()
			.get(VEICULO_ID)
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatus404_QuandoDeletarVeiculoInexistente() {
		given()
			.pathParam("veiculoId", VEICULO_ID_INEXISTENTE)
		.when()
			.delete(VEICULO_ID)
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatus204_QuandoConsultarVeiculoInexistente() {
		given()
			.pathParam("veiculoId", 1L)
		.when()
			.delete(VEICULO_ID)
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeVeiculos_QuandoConsultarVeiculos() {
		given()
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeVeiculosCadastrados));
	}
	
	@Test
	public void deveRetornarStatusCode200_QuandoConsultarVeiculosComMarca() {
		given()
			.params("marca", jetta.getMarca())
		.when()
			.get(POR_MARCA_OU_ANO)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarStatusCode400_QuandoConsultarVeiculosComMarca() {
		given()
			.params("marca", "FORD")
		.when()
			.get(POR_MARCA_OU_ANO)
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	public void deveRetornarStatusCode200_QuandoConsultarVeiculosComAno() {
		given()
			.params("ano", 2019)
		.when()
			.get(POR_MARCA_OU_ANO)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarUmaListaVazia_QuandoConsultarVeiculosComAnoIneXistente() {
		given()
			.params("ano", 20)
		.when()
			.get(POR_MARCA_OU_ANO)
		.then()
			.body("", hasSize(0));
	}
	
	@Test
	public void deveRetornarStatusCode200_QuandoConsultarVeiculosComMarcaOuAno() {
		given()
			.params("marca", jetta.getMarca())
			.params("ano", 2030)
		.when()
			.get(POR_MARCA_OU_ANO)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarUmaListaVazia_QuandoConsultarVeiculosComMarcaOuAnoSemPassarNenhumParametro() {
		given()
		.when()
			.get(POR_MARCA_OU_ANO)
		.then()
		.body("", hasSize(0));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarVeiculo() {
		given()
	        .body(jsonCorretoVeiculo)
	    .when()
	        .post()
	    .then()
	        .statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarStatus200_QuandoAtualizarParcialmenteUmVeiculo() {
		given()
			.body(jsonVeiculoParcial)
			.pathParam("veiculoId", 1L)
		.when()
			.patch(VEICULO_ID)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatus404_QuandoAtualizarVeiculoInexistente() {
		given()
	        .body(jsonVeiculoParcial)
	        .pathParam("veiculoId", VEICULO_ID_INEXISTENTE)
	    .when()
	        .patch(VEICULO_ID)
	    .then()
	        .statusCode(HttpStatus.NOT_FOUND.value());
	}
	
}
