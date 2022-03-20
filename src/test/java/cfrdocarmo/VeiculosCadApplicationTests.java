package cfrdocarmo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
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
import io.restassured.http.ContentType;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class VeiculosCadApplicationTests {
	
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
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorreto_QuandoConsultarVeiculoExistente() {
		given()
			.pathParam("veiculoId", jetta.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{veiculoId}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatus404_QuandoConsultarVeiculoInexistente() {
		given()
			.pathParam("veiculoId", VEICULO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{veiculoId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatus404_QuandoDeletarVeiculoInexistente() {
		given()
			.pathParam("veiculoId", VEICULO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.delete("/{veiculoId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatus204_QuandoConsultarVeiculoInexistente() {
		given()
			.pathParam("veiculoId", 1L)
			.accept(ContentType.JSON)
		.when()
			.delete("/{veiculoId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeVeiculos_QuandoConsultarVeiculos() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeVeiculosCadastrados));
	}
	
	@Test
	public void deveRetornarStatusCode200_QuandoConsultarVeiculosComMarca() {
		given().log().all()
			.params("marca", jetta.getMarca())
			.accept(ContentType.JSON)
		.when()
			.get("/porMarcaOuAno")
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarStatusCode400_QuandoConsultarVeiculosComMarca() {
		given().log().all()
			.params("marca", "FORD")
			.accept(ContentType.JSON)
		.when()
			.get("/porMarcaOuAno")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	public void deveRetornarStatusCode200_QuandoConsultarVeiculosComAno() {
		given().log().all()
			.params("ano", 2019)
			.accept(ContentType.JSON)
		.when()
			.get("/porMarcaOuAno")
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarUmaListaVazia_QuandoConsultarVeiculosComAnoIneXistente() {
		given().log().all()
			.params("ano", 20)
			.accept(ContentType.JSON)
		.when()
			.get("/porMarcaOuAno")
		.then()
			.body("", hasSize(0));
	}
	
	@Test
	public void deveRetornarStatusCode200_QuandoConsultarVeiculosComMarcaOuAno() {
		given().log().all()
			.params("marca", jetta.getMarca())
			.params("ano", 2030)
			.accept(ContentType.JSON)
		.when()
			.get("/porMarcaOuAno")
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarUmaListaVazia_QuandoConsultarVeiculosComMarcaOuAnoSemPassarNenhumParametro() {
		given().log().all()
			.accept(ContentType.JSON)
		.when()
			.get("/porMarcaOuAno")
		.then()
		.body("", hasSize(0));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarVeiculo() {
		given()
	        .body(jsonCorretoVeiculo)
	        .contentType(ContentType.JSON)
	        .accept(ContentType.JSON)
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
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.patch("/{veiculoId}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatus404_QuandoAtualizarVeiculoInexistente() {
		given()
	        .body(jsonVeiculoParcial)
	        .pathParam("veiculoId", VEICULO_ID_INEXISTENTE)
	        .contentType(ContentType.JSON)
	        .accept(ContentType.JSON)
	    .when()
	        .patch("/{veiculoId}")
	    .then()
	        .statusCode(HttpStatus.NOT_FOUND.value());
	}

}
