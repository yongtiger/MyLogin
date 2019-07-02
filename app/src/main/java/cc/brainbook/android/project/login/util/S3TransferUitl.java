package cc.brainbook.android.project.login.util;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.net.URL;

import cc.brainbook.android.project.login.config.Config;
import cc.brainbook.android.project.login.useraccount.modify.ModifyActivity;

/*
 * Handles basic helper functions used throughout the app.
 */
///https://aws.amazon.com/cn/blogs/mobile/introducing-the-transfer-utility-for-the-aws-sdk-for-android/
public class S3TransferUitl {
    private AmazonS3Client sS3Client;
    private CognitoCachingCredentialsProvider sCredProvider;
    private TransferUtility sTransferUtility;

    /**
     * Gets an instance of CognitoCachingCredentialsProvider which is constructed using the given Context.
     *
     * @param context An Context instance.
     * @return A default credential provider.
     */
    private CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            try {
                sCredProvider = new CognitoCachingCredentialsProvider(
                        context.getApplicationContext(),
                        Config.COGNITO_POOL_ID,
                        Regions.fromName(Config.COGNITO_POOL_REGION));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sCredProvider;
    }

    /**
     * Gets an instance of a S3 client which is constructed using the given Context.
     *
     * @param context An Context instance.
     * @return A default S3 client.
     */
    private AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            try {
                final CognitoCachingCredentialsProvider credProvider = getCredProvider(context.getApplicationContext());
                if (null != credProvider) {
                    sS3Client = new AmazonS3Client(credProvider);
                    sS3Client.setRegion(Region.getRegion(Regions.fromName(Config.BUCKET_REGION)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sS3Client;
    }

    /**
     * Gets an instance of the TransferUtility which is constructed using the given Context
     * 
     * @param context
     * @return a TransferUtility instance
     */
    public TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            try {
                sTransferUtility = TransferUtility.builder()
                        .context(context.getApplicationContext())
                        .s3Client(getS3Client(context.getApplicationContext()))
                        .defaultBucket(Config.BUCKET_NAME)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sTransferUtility;
    }

    /**
     * 生成预签名对象 URL
     *
     * @param key
     * @return
     */
    public String getSignatureUrl(Context context, String key) {
        //获取一个request
        final GeneratePresignedUrlRequest urlRequest =
                new GeneratePresignedUrlRequest(Config.BUCKET_NAME, key);
//                        Date expirationDate = null;
//                        try {
//                            expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        //设置过期时间
//                        urlRequest.setExpiration(expirationDate);

        //生成公用的url
        final URL url = getS3Client(context).generatePresignedUrl(urlRequest);
        return url.toString();
    }
}
