package com.backend.NawaBari.api;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@RestController
public class LoginController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public LoginController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/loginSuccess")
    public RedirectView getLoginInfo(Principal principal) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService
                .loadAuthorizedClient("kakao", principal.getName());

        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        String redirectUri = "myapp://callback";
        String finalRedirectUri = String.format("%s?access-token=%s", redirectUri, accessToken);

        return new RedirectView(finalRedirectUri);
    }
}