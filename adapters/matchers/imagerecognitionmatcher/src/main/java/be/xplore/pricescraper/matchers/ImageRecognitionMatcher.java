package be.xplore.pricescraper.matchers;

import be.xplore.pricescraper.exceptions.MatchException;
import be.xplore.pricescraper.exceptions.MatcherNotInitializedException;
import jakarta.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest;
import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import software.amazon.awssdk.services.rekognition.model.TextDetection;

/**
 * This is an {@link ItemMatcher} implementation.
 * The implmentation is based on image recognition.
 */
public class ImageRecognitionMatcher extends ItemMatcher {

  private static final LevenshteinDistance levenshteinDistance =
      LevenshteinDistance.getDefaultInstance();
  private static final double matchThreshold = 0.8;
  private static final int textDetectionLimit = 2;
  private final Environment env;

  protected ImageRecognitionMatcher(Environment env) {
    this.env = env;
  }

  @Override
  public boolean matchingIsPossible() {
    return getItemA().getImage() != null && getItemB().getImage() != null
        && !getItemA().getImage().isEmpty() && !getItemB().getImage().isEmpty();
  }

  @Override
  public boolean isMatching() {
    if (!isInitialized()) {
      throw new MatcherNotInitializedException();
    }
    double matchProbability = getMatchProbabilityInPercentage();
    return matchProbability >= matchThreshold;
  }

  @Override
  public double getMatchProbabilityInPercentage() {
    List<List<String>> linesFromImages = getSortedLinesFromImages();
    int total = getTotalDifferenceInCharsFromStringLists(linesFromImages);
    List<List<String>> labelsFromImages = getSortedLabelsFromImages();
    total += getTotalDifferenceInCharsFromStringLists(labelsFromImages);
    return normalizeScoreToPercentageGivenRange(total, 0, 500);
  }

  private int getTotalDifferenceInCharsFromStringLists(List<List<String>> linesFromImages) {
    if (linesFromImages.size() != 2) {
      throw new MatchException("Lines from images were incorrectly formed");
    }
    int totalDifferenceInChars = 0;
    for (int i = 0; i < linesFromImages.get(0).size(); i++) {
      int subtotal =
          levenshteinDistance.apply(linesFromImages.get(0).get(i), linesFromImages.get(1).get(i));
      totalDifferenceInChars += subtotal;
    }
    return totalDifferenceInChars;
  }

  private List<List<String>> getSortedLinesFromImages() {
    return getSortedStringLists(getLinesFromImages());
  }

  private List<List<String>> getLinesFromImages() {
    List<TextDetection> detectionsA = detectTextLabelsForImageFromUrl(getItemA().getImage());
    List<TextDetection> detectionsB = detectTextLabelsForImageFromUrl(getItemB().getImage());
    List<String> linesA =
        getLineAsStringFromDetections(detectionsA.subList(0, textDetectionLimit));
    List<String> linesB =
        getLineAsStringFromDetections(detectionsB.subList(0, textDetectionLimit));
    List<List<String>> lines = new ArrayList<>();
    lines.add(linesA);
    lines.add(linesB);
    return lines;
  }

  private List<String> getLineAsStringFromDetections(List<TextDetection> detections) {
    return detections.stream().map(TextDetection::detectedText).toList();
  }

  private List<TextDetection> detectTextLabelsForImageFromUrl(String imageUrl) {
    AwsBasicCredentials awsBasicCredentials =
        getAwsCredentials();
    try (RekognitionClient rekognitionClient = RekognitionClient.builder()
        .region(Region.EU_WEST_1)
        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
        .build()) {
      SdkBytes sourceBytes = getSdkBytes(imageUrl);
      Image souImage = Image.builder()
          .bytes(sourceBytes)
          .build();

      DetectTextRequest textRequest = DetectTextRequest.builder()
          .image(souImage)
          .build();

      DetectTextResponse textResponse = rekognitionClient.detectText(textRequest);
      return textResponse.textDetections();
    } catch (RekognitionException | IOException ignored) {
      throw new MatchException("Error while making aws request");
    }
  }

  private List<List<String>> getSortedLabelsFromImages() {
    return getSortedStringLists(getLabelsFromImages());
  }

  private List<List<String>> getSortedStringLists(List<List<String>> stringLists) {
    for (int i = 0; i < stringLists.size(); i++) {
      List<String> l = new ArrayList<>(stringLists.get(i));
      l.sort(String::compareToIgnoreCase);
      stringLists.set(i, l);
    }
    return stringLists;
  }


  private List<List<String>> getLabelsFromImages() {
    List<Label> detectionsA = detectImageLabels(getItemA().getImage());
    List<Label> detectionsB = detectImageLabels(getItemA().getImage());
    List<String> labelsA =
        getLabelAsStringFromDetections(detectionsA.subList(0, textDetectionLimit));
    List<String> labelsB =
        getLabelAsStringFromDetections(detectionsB.subList(0, textDetectionLimit));
    List<List<String>> labels = new ArrayList<>();
    labels.add(labelsA);
    labels.add(labelsB);
    return labels;
  }

  private List<String> getLabelAsStringFromDetections(List<Label> detections) {
    return detections.stream().map(Label::toString).toList();
  }

  private List<Label> detectImageLabels(String imageUrl) {
    AwsBasicCredentials awsBasicCredentials =
        getAwsCredentials();
    try (RekognitionClient rekognitionClient = RekognitionClient.builder()
        .region(Region.EU_WEST_1)
        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
        .build()) {
      SdkBytes sourceBytes = getSdkBytes(imageUrl);
      // Create an Image object for the source image.
      Image souImage = Image.builder()
          .bytes(sourceBytes)
          .build();

      DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
          .image(souImage)
          .maxLabels(10)
          .build();

      DetectLabelsResponse labelsResponse = rekognitionClient.detectLabels(detectLabelsRequest);
      return labelsResponse.labels();
    } catch (RekognitionException | IOException e) {
      throw new MatchException("Failed to detect image labels");
    }
  }

  private SdkBytes getSdkBytes(String imageUrl) throws IOException {
    SdkBytes sourceBytes = null;
    if (imageUrl.startsWith("http")) {
      InputStream sourceStream = new URL(imageUrl).openStream();
      sourceBytes = SdkBytes.fromInputStream(sourceStream);
    } else if (imageUrl.startsWith("data")) {
      sourceBytes = SdkBytes.fromByteArray(parse64DataUri(imageUrl));
    }
    if (sourceBytes == null) {
      throw new MatchException("Could not determine SdkBytes, is the image url correct?");
    }
    return sourceBytes;
  }

  private byte[] parse64DataUri(String dataUri) {
    String data = dataUri.split("base64,")[1];
    return DatatypeConverter.parseBase64Binary(data);
  }

  private AwsBasicCredentials getAwsCredentials() {
    String awsAccessKeyId = env.getProperty("aws_access_key_id");
    String awsSecretAccessKey = env.getProperty("aws_secret_access_key");
    return AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey);
  }
}
