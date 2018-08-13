package com.appinsnap.ewatchIBfinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.alexbbb.uploadservice.AbstractUploadServiceReceiver;
import com.alexbbb.uploadservice.ContentType;
import com.alexbbb.uploadservice.UploadRequest;
import com.alexbbb.uploadservice.UploadService;
/*
import com.gazuntite.provider.R;
import com.gazuntite.provider.model.DocumentSaveResponseModel;
import com.gazuntite.provider.model.docUploadParams;
import com.gazuntite.provider.utility.Constants;
import com.gazuntite.provider.utility.ImageScalingUtils;
import com.gazuntite.provider.utility.Session;
import com.gazuntite.provider.utility.Utility;*/

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UploadFiles extends AsyncTask<Void, Void, Void> {
    public static final String TAG = UploadFiles.class.getSimpleName();
    public static final String DEFAULT_UPLOAD_DOCUMENT = "pat/uploadDocument";
    public static final String NOTE_UPLOAD_DOCUMENT = "note/uploadNoteAttachment";
    private final ArrayList<String> fileType;
    private String Webkey;
    private String access_token;

    private Context mContext;
   /* private List<docUploadParams> mItems;
    int mPatientId = -1, mCounter = 0, mProviderId = -1, docId = -1, parentId = -1;
    private Constants.recTypes mRecType;
    private ArrayList<DocumentSaveResponseModel> mResponseList;
    private ArrayList<Uri> mDocumentsList;
    private ArrayList<String> mPathsList;
    private ArrayList<String> mFileNamesList;
    private String URL_API = Constants.PORTAL_URL + DEFAULT_UPLOAD_DOCUMENT;
    private long folderId = Constants.FOLDER_GENERAL;*/
   int mCounter = 0;
    private boolean mImageFileRenameFlag = false;
    private long noteId;
    private String metaData;
    ArrayList<String> Items;
    String dataId;
    boolean isAc;

//    public abstract void allDone(ArrayList<DocumentSaveResponseModel> responseList);

    public UploadFiles(Context context, ArrayList<String> Items, ArrayList<String> fileType, String dataId, String access_token, String webkey, boolean isAc) {
        this.mCounter = 0;
        this.mContext = context;
        this.Items=Items;
        this.dataId=dataId;
        this.fileType=fileType;
        this.access_token=access_token;
        this.Webkey=webkey;
        this.isAc=isAc;


    }

   /* public UploadFiles(Context context, int patientId, int providerId, Constants.recTypes recType, ArrayList<Uri> documentsList, int existingDocId, int parentFolderId, boolean renameFlag, String metadata) {
        this.mCounter = 0;
        this.mContext = context;
        this.mPatientId = patientId;
        this.mProviderId = providerId;
        this.mRecType = recType;
        this.mDocumentsList = documentsList;
        this.docId = existingDocId;
        this.parentId = parentFolderId;
        this.mPathsList = new ArrayList<>();
        this.mItems = new ArrayList<>();
        this.mResponseList = new ArrayList<>();
        this.mFileNamesList = new ArrayList<>();
        this.mImageFileRenameFlag = renameFlag;
        this.metaData = metadata;
        this.execute();
    }

    public UploadFiles(Context context, int patientId, int providerId, Constants.recTypes recType, ArrayList<Uri> documentsList, int existingDocId, int parentFolderId, boolean renameFlag, long noteID) {
        URL_API = Constants.PORTAL_URL + NOTE_UPLOAD_DOCUMENT;
        this.mCounter = 0;
        this.mContext = context;
        this.mPatientId = patientId;
        this.mProviderId = providerId;
        this.mRecType = recType;
        this.mDocumentsList = documentsList;
        this.docId = existingDocId;
        this.parentId = parentFolderId;
        this.mPathsList = new ArrayList<>();
        this.mItems = new ArrayList<>();
        this.mResponseList = new ArrayList<>();
        this.mFileNamesList = new ArrayList<>();
        this.mImageFileRenameFlag = renameFlag;
        this.noteId = noteID;
        this.execute();
    }*/

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG, "onPreExecute: mDocumentsList.size: " + Items.size());

       /* for (int pos = 0; pos < mDocumentsList.size(); pos += 1) {
            try {
                Uri uri = mDocumentsList.get(pos);
                String path = "";

                if (uri.toString().contains(Constants.FILE_TYPE_TEXT) || uri.toString().contains(Constants.FILE_TYPE_PDF)) {
                    path = uri.getPath();
                } else if (uri.toString().contains(Constants.FILE_TYPE_WORD_DOC) || uri.toString().contains(Constants.FILE_TYPE_WORD_DOCX)) {
                    path = Utility.getPath(mContext, uri);
                } else {
                    path = Utility.getPath(mContext, mDocumentsList.get(pos));
                }

                mPathsList.add(path);
                Log.i(TAG, "onPreExecute: path: " + path);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }*/
    }

    @Override
    protected Void doInBackground(Void... params) {
        //makeImageFiles();
        Log.d(TAG, "doInBackground: mItems.size: " + Items.size());
        if (Items.size() > 0)
            DoUploadAttachments();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d(TAG, "onPostExecute: mItems.size: " + Items.size());
        if (Items.size() <= 0) {
//            deleteTempFiles();
//            allDone(null);
        }
    }

    private void makeImageFiles() {
       /* File toUpload_image = null;

        for (int pos = 0; pos < mPathsList.size(); pos += 1) {
            try {
                String imgPath;

                if (mPathsList.get(pos).contains("video") || mPathsList.get(pos).contains(Constants.FILE_TYPE_VIDEO_MP4) || mPathsList.get(pos).contains(Constants.FILE_TYPE_VIDEO_3GP) || mPathsList.get(pos).contains(Constants.FILE_TYPE_AUDIO) || mPathsList.get(pos).contains(Constants.FILE_TYPE_CLIP)) {

                    toUpload_image = new File(mPathsList.get(pos));
                } else if (mPathsList.get(pos).contains(Constants.FILE_TYPE_TEXT) || mPathsList.get(pos).contains(Constants.FILE_TYPE_PDF) || mPathsList.get(pos).contains(Constants.FILE_TYPE_WORD_DOC) || mPathsList.get(pos).contains(Constants.FILE_TYPE_WORD_DOCX)) {
                    toUpload_image = new File(mPathsList.get(pos));
                } else {
                    if (mImageFileRenameFlag) {
                        imgPath = mPathsList.get(pos);
                        toUpload_image = new File(imgPath);
                        mFileNamesList.add(imgPath);
                    } else {
                        imgPath = mPathsList.get(pos);
                        String fileName = "attachment-" + Calendar.getInstance().getTimeInMillis() + ".jpg";

                        Bitmap scaledBitmap = ImageScalingUtils.createScaledBitmap(imgPath, 1000, 1000);
                        if (scaledBitmap != null) {
                            toUpload_image = Utility.SaveTempFile(scaledBitmap, fileName);
                            mFileNamesList.add(fileName);

                            if (imgPath.contains("temp")) {
                                mFileNamesList.add(imgPath);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (toUpload_image != null && toUpload_image.exists()) {
                docUploadParams documnet = new docUploadParams();
                documnet.setrType(this.mRecType);

                if (mPathsList.get(pos).contains("video") || mPathsList.get(pos).contains(Constants.FILE_TYPE_VIDEO_MP4) || mPathsList.get(pos).contains(Constants.FILE_TYPE_VIDEO_3GP))
                    documnet.setDocType(Constants.docTypes.Video);
                else if (mPathsList.get(pos).contains(Constants.FILE_TYPE_AUDIO))
                    documnet.setDocType(Constants.docTypes.Audio);
                else if (mPathsList.get(pos).contains(Constants.FILE_TYPE_TEXT) || mPathsList.get(pos).contains(Constants.FILE_TYPE_PDF) || mPathsList.get(pos).contains(Constants.FILE_TYPE_WORD_DOC) || mPathsList.get(pos).contains(Constants.FILE_TYPE_WORD_DOCX))
                    documnet.setDocType(Constants.docTypes.File);
                else if (mPathsList.get(pos).contains(Constants.FILE_TYPE_CLIP)) {
                    documnet.setDocType(Constants.docTypes.Clip);
                } else
                    documnet.setDocType(Constants.docTypes.Image);

                if (this.mRecType.equals(Constants.recTypes.PATIENT)) {
                    documnet.setRecID(mPatientId + "");
                } else if (this.mRecType.equals(Constants.recTypes.NOTES)) {
                    documnet.setRecID(noteId + "");
                } else {
                    documnet.setRecID(mProviderId + "");
                }

                documnet.setFile(toUpload_image);
                documnet.setVisitId("");
                documnet.setUserID(Session.loginUserID);
                mItems.add(documnet);
                Log.d(TAG, "makeImageFiles: mItems.size: " + mItems.size() + ", document.filePath: " + documnet.getFile().getAbsolutePath());
            }
        }*/
    }

    void DoUploadAttachments() {
        Log.d(TAG, "DoUploadAttachments: ");
        final AbstractUploadServiceReceiver uploadReceiver = new AbstractUploadServiceReceiver() {

            @Override
            public void onProgress(String uploadId, int progress) {
                Log.i(TAG, "onProgress(): upload with ID " + uploadId + " is: " + progress);
            }

            @Override
            public void onError(String uploadId, Exception exception) {
                mCounter++;

                String message = "onError(): mCounter: " + mCounter + ", Error in upload with ID: " + uploadId + ". " + exception.getMessage();
                Log.e(TAG, message, exception);

                if (mCounter == Items.size()) {
                    this.unregister(mContext);
//                    deleteTempFiles();
//                    allDone(mResponseList);
                }
            }

            @Override
            public void onCompleted(String uploadId, int serverResponseCode, String serverResponseMessage) {
                mCounter++;
                String message = "onCompleted(): mCounter: " + mCounter + ", Upload with ID " + uploadId + " is completed: responseCode: " + serverResponseCode + ", responseMessage: " + serverResponseMessage;
                Log.i(TAG, message);

                try {
                   /* JSONObject json = new JSONObject(serverResponseMessage);
                    DocumentSaveResponseModel documentSaveResponseModel = new DocumentSaveResponseModel();
                    documentSaveResponseModel.setErrorCode(json.getString("errorCode"));
                    documentSaveResponseModel.setErrorMessage(json.getString("errorMessage"));
                    documentSaveResponseModel.setData(json.getString("data"));
                    mResponseList.add(documentSaveResponseModel);
*/
                } catch (Exception je) {
                    je.printStackTrace();
                }

                if (mCounter == Items.size()) {
                    this.unregister(mContext);
//                    deleteTempFiles();
//                    allDone(mResponseList);
                }
            }
        };

        uploadReceiver.register(mContext);

        for (int pos = 0; pos < Items.size(); pos += 1) {
            sendUploaderRequest(mContext, Items.get(pos),fileType.get(pos), pos);
        }
    }

    public void sendUploaderRequest(Context c, String item, String fileType, int i) {
        String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());
        SharedPreferences pref = c.getSharedPreferences("MyPref", 0); // 0 - for private mode
      String  UserId=   pref.getString("UserId", null); // getting String

        File f= new File(item);
        final UploadRequest request = new UploadRequest(c, "" + i, mContext.getString(R.string.APP_URL)+"/api/ems/UpdateMediaFiles");
//        request.setMethod("Post");

//        request.addParameter("Webkey", Webkey);


        if (fileType.contains("image")) {
            Log.e(TAG, "sendUploaderRequest(): Image");

//            request.addFileToUpload(item, "Image", item, ContentType.IMAGE_JPEG);
            request.addFileToUpload(item, "file", f.getName(), ContentType.IMAGE_JPEG);
            request.addParameter("IssueId", dataId);
            if(isAc){
                request.addParameter("IsACImageAttached", "True");
            }else {
                request.addParameter("IsImageAttached", "True");
            }
//            request.addParameter("IsImageAttached", "True");
//            request.addParameter("Imagename", item);
//            request.addParameter("Image", encodedImage);
            request.addParameter("DateUpdated", date);
            request.addParameter("FileUpdatedBy", UserId);
        }else  if (fileType.contains("video")) {
            Log.e(TAG, "sendUploaderRequest(): video");

//            request.addFileToUpload(item, "Vedio", item, ContentType.VIDEO_MPEG);
            request.addFileToUpload(item, "file", f.getName(),null);
            request.addParameter("IssueId", dataId);
            if(isAc){
                request.addParameter("IsACVideoAttached", "True");
            }else {
                request.addParameter("IsVideoAttached", "True");
            }

//            request.addParameter("IsVideoAttached", "True");
//            request.addParameter("Vedioname", item);
            request.addParameter("DateUpdated", date);
            request.addParameter("FileUpdatedBy", UserId);
        }else if (fileType.contains("audio")) {
            Log.e(TAG, "sendUploaderRequest(): Audio");

//            request.addFileToUpload(item, "Audio", item, ContentType.AUDIO_M3U);
            request.addFileToUpload(item, "file", f.getName(),ContentType.AUDIO_M3U);
            request.addParameter("IssueId", dataId);
            request.addParameter("IsAudioAttached", "True");
//            request.addParameter("Audioname", item);
            request.addParameter("DateUpdated", date);
            request.addParameter("FileUpdatedBy", UserId);
        }

/*
        final UploadRequest request = new UploadRequest(c, "" + i, URL_API);

        if (item.getDocType().equals(Constants.docTypes.Image.id)) {
            Log.e(TAG, "sendUploaderRequest(): Image");
            request.addFileToUpload(item.getFile().getAbsolutePath(), "file", item.getFile().getName(), ContentType.IMAGE_JPEG);
        } else if (item.getDocType().equals(Constants.docTypes.Audio.id)) {
            request.addFileToUpload(item.getFile().getAbsolutePath(), "file", item.getFile().getName(), ContentType.AUDIO_M3U);
            Log.e(TAG, "sendUploaderRequest(): Audio");
        } else if (item.getDocType().equals(Constants.docTypes.Video.id)) {
            request.addFileToUpload(item.getFile().getAbsolutePath(), "file", item.getFile().getName(), ContentType.VIDEO_MPEG);
            Log.e(TAG, "sendUploaderRequest(): Video");
        } else if (item.getDocType().equals(Constants.docTypes.File.id)) {
            String path = item.getFile().getAbsolutePath();
            if (path.contains(Constants.FILE_TYPE_PDF)) {
                request.addFileToUpload(path, "file", item.getFile().getName(), ContentType.APPLICATION_PDF);
                Log.e(TAG, "sendUploaderRequest(): PDF, path: " + path);
            } else if (path.contains(Constants.FILE_TYPE_TEXT)) {
                request.addFileToUpload(path, "file", item.getFile().getName(), ContentType.TEXT_PLAIN);
                Log.e(TAG, "sendUploaderRequest(): Text, path: " + path);
            } else if (path.contains(Constants.FILE_TYPE_WORD_DOC) || path.contains(Constants.FILE_TYPE_WORD_DOCX)) {
                request.addFileToUpload(path, "file", item.getFile().getName(), ContentType.APPLICATION_MS_WORD);
                Log.e(TAG, "sendUploaderRequest(): WORD, path: " + path);
            }
        } else if (item.getDocType().equals(Constants.docTypes.Clip.id)) {
            request.addFileToUpload(item.getFile().getAbsolutePath(), "file", item.getFile().getName(), "text/html");
            Log.i(TAG, "sendUploaderRequest(): Clip, path: " + path);
        }

        if (folderId > -1) { // Don't change this parameter as it is related to GENERAL_FOLDER, according to backend requirement.
            Log.e(TAG, "sendUploaderRequest(): folderId: " + folderId);
            request.addParameter("folderId", folderId + "");
        }

        request.addParameter("rType", item.rType);
        request.addParameter("recId", item.recID);
        request.addParameter("metadata", metaData);
        request.addParameter("docType", item.docType);
        request.addParameter("visitId", item.visitId);
        request.addParameter("patientId", String.valueOf(mPatientId));
        request.addParameter("providerId", String.valueOf(mProviderId));
        if (docId > 0) // Used in case when editing an existing text document.
        {
            Log.e(TAG, "sendUploaderRequest(): docId: " + docId);
            request.addParameter("docId", String.valueOf(docId));
        }

        if (parentId > 0) // Used when user is inside a folder and uploads or edit a document.
        {
            Log.e(TAG, "sendUploaderRequest(): parentId: " + parentId);
            request.addParameter("parentId", String.valueOf(parentId));
        }
        */


//        request.addHeader("Authorization",access_token);

        request.setNotificationConfig(R.drawable.ib_logo_new,
                "Uploading Files",
                "Upload in Progress",
                "Upload Completed Successfully",
                "Error in Uploading",
                true);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        System.setProperty("http.keepAlive", "false");

        try {
            UploadService.startUpload(request);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void deleteTempFiles() {
   /*     Log.d(TAG, "deleteTempFiles: mFileNamesList.size: " + mFileNamesList.size());
        File file;
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        for (int pos = 0; pos < mFileNamesList.size(); pos++) {
            String fileName = mFileNamesList.get(pos);
            if (fileName.contains("temp")) {
                file = new File(fileName);
            } else {
                file = new File(extStorageDirectory, fileName);
            }
            boolean isDeleted = file.delete();
            Log.d(TAG, file.toString() + " deleted " + isDeleted);
        }*/
    }
}
