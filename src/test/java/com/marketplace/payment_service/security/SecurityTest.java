package com.marketplace.payment_service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifie les regles de securite : 401 sans jeton, 200 avec jeton, 403 si role insuffisant.
 */
@SpringBootTest
class SecurityTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtService jwtService;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    private String bearer(Long id, String email, String role) {
        return "Bearer " + jwtService.generateToken(id, email, role);
    }

    @Test
    void sansJeton_401() throws Exception {
        mvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jetonInvalide_401() throws Exception {
        mvc.perform(get("/api/me").header("Authorization", "Bearer faux.jeton.invalide"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void avecJeton_200() throws Exception {
        mvc.perform(get("/api/me").header("Authorization", bearer(1L, "ali@test.com", "ACHETEUR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ali@test.com"))
                .andExpect(jsonPath("$.role").value("ACHETEUR"));
    }

    @Test
    void jetonDeTest_standalone() throws Exception {
        mvc.perform(post("/api/dev/token/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("VENDEUR"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void utilisateurInconnu_404() throws Exception {
        mvc.perform(post("/api/dev/token/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
    }

    @Test
    void routeAdmin_refuseeAUnAcheteur_403() throws Exception {
        mvc.perform(get("/api/admin/ping").header("Authorization", bearer(1L, "ali@test.com", "ACHETEUR")))
                .andExpect(status().isForbidden());
    }

    @Test
    void routeAdmin_accepteeAUnAdmin_200() throws Exception {
        mvc.perform(get("/api/admin/ping").header("Authorization", bearer(3L, "admin@test.com", "ADMIN")))
                .andExpect(status().isOk());
    }
}