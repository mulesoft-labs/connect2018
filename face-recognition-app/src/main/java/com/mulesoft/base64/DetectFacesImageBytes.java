package com.mulesoft.base64;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

import com.amazonaws.ClientConfiguration;
//import com.amazonaws.services.rekognition.AmazonRekognition;
//import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.Request;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.handlers.RequestHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.Face;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.ListFacesRequest;
import com.amazonaws.services.rekognition.model.ListFacesResult;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.amazonaws.services.rekognition.model.SearchFacesRequest;
import com.amazonaws.services.rekognition.model.SearchFacesResult;
//import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
//import com.amazonaws.services.rekognition.model.Face;
//import com.amazonaws.services.rekognition.model.FaceMatch;
//import com.amazonaws.services.rekognition.model.FaceRecord;
//import com.amazonaws.services.rekognition.model.Image;
//import com.amazonaws.services.rekognition.model.IndexFacesRequest;
//import com.amazonaws.services.rekognition.model.IndexFacesResult;
//import com.amazonaws.services.rekognition.model.ListFacesRequest;
//import com.amazonaws.services.rekognition.model.ListFacesResult;
//import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
//import com.amazonaws.services.rekognition.model.SearchFacesRequest;
//import com.amazonaws.services.rekognition.model.SearchFacesResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.Base64;
import com.amazonaws.util.TimingInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Purpose of DetectFaceImageBytes: searches the AWS Rekognition service for a
 * face in the collection of faces and return the closest five matches
 * 
 * requirements for this class: 1. pass in the base64key as an attachment ==>
 * base64Key 2. pass in the access key and access secret for AWS ==>
 * access_key_id and secret_key_id 3. prebuild the collection in AWS Rekognition
 * and pass in the name of the key ==> collectionid
 * 
 * @author brandon.grantham returns the search response
 * 
 */
@SuppressWarnings("deprecation")
public class DetectFacesImageBytes extends AbstractMessageTransformer implements RequestHandler {
	private static final Log log = LogFactory.getLog(DetectFacesImageBytes.class);
	private AmazonRekognition rekognition;

	private static final String S3_BUCKET = "connectdemo2017-uridemo-bucket";
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Map<String, ObjectMetadata> transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {

		Map<String, ObjectMetadata> omdMap = new LinkedHashMap<String, ObjectMetadata>();
		Map<String, ObjectMetadata> muleOmdMap = new LinkedHashMap<String, ObjectMetadata>();

		try {

			// int mFaces = message.getProperty("maxFaces",
			// PropertyScope.INVOCATION);
			String collectionId = message.getInvocationProperty("collectionid").toString();
			String msg = (String) message.getPayload();

//			final String accessId = message.getProperty("access_key_id", PropertyScope.INVOCATION);
			final String accessId = "AKIAJA52DG6GGOKYUA6Q";
			// TODO figure out why the property cannot be passed in as a flow
			// class.getProtectionDomain().getCodeSource()
//			final String secretId = "KeXBRvyyvNMNxPlNu2hkkJkKo5KC+wvapvpbOEbi";
			final String secretId = "2REtdqqhdBOn/pR08AYMPVlnc+dAoQ+7y2suDDeS";
//									"2REtdqqhdBOn/pR08AYMPVlnc+dAoQ+7y2suDDeS";
			// AmazonS3 s3 = AmazonS3ClientBuilder.standard()
			// .withCredentials(new AWSStaticCredentialsProvider(new
			// BasicAWSCredentials(accessId, secretId)))
			// .build();

			AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(accessId, secretId));
			Region usWest2 = Region.getRegion(Regions.US_WEST_2);
			s3.setRegion(usWest2);

			BasicAWSCredentials bcawsc = new BasicAWSCredentials(accessId, secretId);

			AWSStaticCredentialsProvider scp = new AWSStaticCredentialsProvider(bcawsc);

			AmazonRekognitionClientBuilder rekognitionClientbldr = AmazonRekognitionClientBuilder.standard();

			rekognitionClientbldr.setCredentials(scp);// .withRegion(usWest2);

			AmazonRekognition rekognitionClient = rekognitionClientbldr.withRegion("us-west-2").build();// new
			byte[] decodeResult;
			try {
				String cleanMsgv = msg.trim();
				decodeResult = Base64.decode(cleanMsgv.getBytes());
			} catch (java.lang.IllegalArgumentException e) {
				// TODO Auto-generated catch block
				decodeResult = java.util.Base64.getMimeDecoder().decode(msg.getBytes());
			}
			// AmazonRekognition rekognitionClient =
			// AmazonRekognitionClientBuilder.standard()
			// .withCredentials(new AWSStaticCredentialsProvider(new
			// BasicAWSCredentials(accessId, secretId)))
			// .build();
			// .withRegion(Regions.US_WEST_2)
			// ByteBuffer.wrap(Base64.decode(msg.getBytes()));
			SearchFacesByImageRequest sfbir = getsearchFacesByImageUtil(collectionId, ByteBuffer.wrap(decodeResult),
					10);
			// new
			// SearchFacesByImageRequest().withCollectionId(collectionId).withImage(new
			// Image()
			// .withBytes(ByteBuffer.wrap(Base64.decode(msg.getBytes())).withMaxFaces(5).withFaceMatchThreshold((float)
			// 0.0);
			// getsearchFacesByImageUtil(collectionId,ByteBuffer.wrap(Base64.decode(msg.getBytes())),
			// 10));
			SearchFacesByImageResult response = rekognitionClient.searchFacesByImage(sfbir);
			List<String> imageIds = new ArrayList<String>();
			ListFacesResult listFacesResult = null;
			String paginationToken = null;
			Map<String, String> detectedFace = new HashMap<String, String>();
			for (FaceMatch fm : response.getFaceMatches()) {
				detectedFace.put(fm.getFace().getExternalImageId(), fm.getSimilarity().toString());
				System.out.println("faceMatch" + fm);
				imageIds.add(fm.getFace().getExternalImageId());
			}
			muleOmdMap = prepareMetadata(s3, imageIds, omdMap, detectedFace);

			// do {
			// if (listFacesResult != null) {
			// paginationToken = listFacesResult.getNextToken();
			// }
			// listFacesResult = callListFaces(collectionId, 1, paginationToken,
			// rekognitionClient);
			// List<Face> faces = listFacesResult.getFaces();
			// for (Face face : faces) {
			// if (face.getFaceId().equals(detectedFace)) {
			// imageIds.add(face.getExternalImageId());
			// System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
			// }
			// }
			// } while (listFacesResult != null &&
			// listFacesResult.getNextToken() != null);

			// s3.getObject(getObjectRequest)

			// muleOmdMap = prepareMetadata(s3, imageIds, omdMap);

			// URL url =
			// com.mulesoft.base64.S3GenerateURL.generateURL(S3_BUCKET, imageId,
			// s3);
			// S3GenerateURL.generateURL(S3_BUCKET, imageId, s3);
			// jsonObject.put("name", omd.getUserMetadata().get("name"));
			// jsonObject.put("title", omd.getUserMetadata().get("title"));
			// jsonObject.put("url", url);
			// jsonArray.put(jsonObject);

			// message.setInvocationProperty("searchResponse", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return muleOmdMap;
	}

	private static Map prepareMetadata(AmazonS3 s3, List<String> imageIds,
			Map<String, ObjectMetadata> omdMap, Map<String, String> faceMap) {
		URL url;
		
		Map<String, Map<?, ?>> tree = new LinkedHashMap<String, Map<?, ?>>();
		for (String id : imageIds) {

			url = S3GenerateURL.generateURL(S3_BUCKET, id, s3);
			ObjectMetadata tmp = s3.getObject(S3_BUCKET, id).getObjectMetadata();
			tmp.addUserMetadata("md_" + "s3Url", url.toString());
			tmp.addUserMetadata("Similarity", faceMap.get(id));
			// tmp.addUserMetadata("Probability", value);
			tree.put(id, tmp.getUserMetadata());
			omdMap.put(id, tmp);
		}
		return tree;
	}

	private static ListFacesResult callListFaces(String collectionId, int limit, String paginationToken,
			AmazonRekognition amazonRekognition) {
		ListFacesRequest listFacesRequest = new ListFacesRequest().withCollectionId(collectionId).withMaxResults(limit)
				.withNextToken(paginationToken);
		return amazonRekognition.listFaces(listFacesRequest);
	}

	private static SearchFacesByImageRequest getsearchFacesByImageUtil(String collectionid, ByteBuffer imageBytes,
			int maxFaces) {
		return new SearchFacesByImageRequest().withCollectionId(collectionid)
				.withImage(new Image().withBytes(imageBytes)).withMaxFaces(10).withFaceMatchThreshold((float) 0.0);
	}

	@Override
	public void beforeRequest(Request<?> request) {
		JSONObject jsonObject = new JSONObject();

	}

	@Override
	public void afterResponse(Request<?> request, Object response, TimingInfo timingInfo) {
		System.out.println(
				com.fasterxml.jackson.databind.ObjectMapper.class.getProtectionDomain().getCodeSource().getLocation());
	}

	@Override
	public void afterError(Request<?> request, Exception e) {
		// TODO Auto-generated method stub
		// String secretId = message.getProperty("secret_key_id",
		// PropertyScope.INVOCATION);

	}

	public SearchFacesResult searchFaces(String idFace, String idCollection) {
		SearchFacesRequest request = new SearchFacesRequest().withFaceId(idFace).withCollectionId(idCollection);
		try {
			SearchFacesResult result = rekognition.searchFaces(request);
			return result;
		} catch (AmazonRekognitionException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<SearchFacesResult> indexAll(ByteBuffer image, String collectionId, String collectionCache) {
		IndexFacesResult indexFacesResult = indexFaces(image, collectionCache);
		List<FaceRecord> list = indexFacesResult.getFaceRecords();
		ArrayList<SearchFacesResult> listResult = new ArrayList<SearchFacesResult>();
		int length = list.size();
		for (int i = 0; i < length; i++) {
			SearchFacesResult result = searchFaces(list.get(i).getFace().getFaceId(), collectionId);
			listResult.add(result);

		}
		return listResult;
	}

	public IndexFacesResult indexFaces(ByteBuffer image, String collectionId) {
		IndexFacesRequest request = new IndexFacesRequest().withCollectionId(collectionId)
				.withImage(new Image().withBytes(image));
		try {
			IndexFacesResult result = rekognition.indexFaces(request);
			return result;
		} catch (AmazonRekognitionException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

}
