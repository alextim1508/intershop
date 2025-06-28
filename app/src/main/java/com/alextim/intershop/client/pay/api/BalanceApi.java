package com.alextim.intershop.client.pay.api;

import com.alextim.intershop.client.pay.invoker.ApiClient;

import com.alextim.intershop.client.pay.dto.BalanceResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", comments = "Generator version: 7.12.0")
public class BalanceApi {
    private ApiClient apiClient;

    public BalanceApi() {
        this(new ApiClient());
    }

    @Autowired
    public BalanceApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * Get user&#39;s current balance
     * 
     * <p><b>200</b> - Current balance retrieved successfully
     * <p><b>404</b> - User not found
     * <p><b>500</b> - Internal server error
     * @param userId User&#39;s ID
     * @return BalanceResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec balanceGetRequestCreation(Long userId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new WebClientResponseException("Missing the required parameter 'userId' when calling balanceGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "userId", userId));
        
        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "bearerAuth" };

        ParameterizedTypeReference<BalanceResponse> localVarReturnType = new ParameterizedTypeReference<BalanceResponse>() {};
        return apiClient.invokeAPI("/balance", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get user&#39;s current balance
     * 
     * <p><b>200</b> - Current balance retrieved successfully
     * <p><b>404</b> - User not found
     * <p><b>500</b> - Internal server error
     * @param userId User&#39;s ID
     * @return BalanceResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<BalanceResponse> balanceGet(Long userId) throws WebClientResponseException {
        ParameterizedTypeReference<BalanceResponse> localVarReturnType = new ParameterizedTypeReference<BalanceResponse>() {};
        return balanceGetRequestCreation(userId).bodyToMono(localVarReturnType);
    }

    /**
     * Get user&#39;s current balance
     * 
     * <p><b>200</b> - Current balance retrieved successfully
     * <p><b>404</b> - User not found
     * <p><b>500</b> - Internal server error
     * @param userId User&#39;s ID
     * @return ResponseEntity&lt;BalanceResponse&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<BalanceResponse>> balanceGetWithHttpInfo(Long userId) throws WebClientResponseException {
        ParameterizedTypeReference<BalanceResponse> localVarReturnType = new ParameterizedTypeReference<BalanceResponse>() {};
        return balanceGetRequestCreation(userId).toEntity(localVarReturnType);
    }

    /**
     * Get user&#39;s current balance
     * 
     * <p><b>200</b> - Current balance retrieved successfully
     * <p><b>404</b> - User not found
     * <p><b>500</b> - Internal server error
     * @param userId User&#39;s ID
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec balanceGetWithResponseSpec(Long userId) throws WebClientResponseException {
        return balanceGetRequestCreation(userId);
    }
}
