package com.pivotallabs.api;

import static com.pivotallabs.util.Strings.asStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.pivotallabs.util.Pair;

public class TestApiGateway extends ApiGateway {

	List<Pair<ApiRequest, ApiResponseCallbacks>>	pendingRequests	= new ArrayList<Pair<ApiRequest, ApiResponseCallbacks>>();

	@Override
	public void makeRequest(final ApiRequest apiRequest, final ApiResponseCallbacks responseCallbacks) {
		pendingRequests.add(Pair.of(apiRequest, responseCallbacks));
	}

	public void simulateResponse(final int httpCode, final String responseBody) throws IOException, SAXException, ParserConfigurationException {
		ensurePendingRequests();
		final XmlApiResponse apiResponse = new XmlApiResponse(httpCode);
		apiResponse.consumeResponse(asStream(responseBody));
		dispatch(apiResponse, unshiftEarliestRequest().b);
	}

	public ApiRequest<?> getLatestRequest() {
		ensurePendingRequests();
		return pendingRequests.get(pendingRequests.size() - 1).a;
	}

	private void ensurePendingRequests() {
		if (pendingRequests.isEmpty()) {
			throw new RuntimeException("No pending requests to simulate response for");
		}
	}

	private Pair<ApiRequest, ApiResponseCallbacks> unshiftEarliestRequest() {
		return pendingRequests.remove(0);
	}
}
