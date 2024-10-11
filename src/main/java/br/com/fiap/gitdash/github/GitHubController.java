package br.com.fiap.gitdash.github;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/")
    public String getUserInfo(Model model, 
                              @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient,
                              @AuthenticationPrincipal OAuth2User oauthUser) {

        // Obter o token de acesso OAuth2
        String tokenValue = authorizedClient.getAccessToken().getTokenValue();

        // Obter os dados do usuário autenticado a partir do OAuth2User
        String name = oauthUser.getAttribute("name");
        String avatarUrl = oauthUser.getAttribute("avatar_url");
        String githubProfileUrl = oauthUser.getAttribute("html_url");

        // Adicionar os dados do usuário ao modelo para repassar para a view
        model.addAttribute("name", name);
        model.addAttribute("avatar_url", avatarUrl);
        model.addAttribute("html_url", githubProfileUrl);

        // Obter a lista de repositórios do usuário e repassar para a view
        List<RepositoryInfo> repos = gitHubService.getUserRepositories(tokenValue);
        model.addAttribute("repos", repos);

        return "user";
    }
}
