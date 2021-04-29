package i.farmer.picture.selector.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author i-farmer
 * @created-time 2021/4/29 4:07 下午
 * @description 本地文件
 */
public class LocalMedia implements Parcelable, ILocalMedia {
    private long id;                // 文件id
    private String path;            // 原路径
    private long duration;          // 视频长度
    private String mimeType;        // 文件类型
    private int width;              // 宽
    private int height;             // 高
    private long size;              // 文件大小
    private String fileName;        // 文件名字
    private String parentFolderName;// 所在目录


    private boolean isCut;          // 是否被裁剪了
    private boolean isCompressed;   // 是否被压缩了
    private boolean isChecked;      // 是否被选中
    private boolean isOriginal;     // 是否被选中了原图
    public boolean isLongImage;     // 是否是长图

    public long getId() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public String getMimeType() {
        return mimeType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getSize() {
        return size;
    }

    public String getFileName() {
        return fileName;
    }

    public String getParentFolderName() {
        return parentFolderName;
    }

    public boolean isCut() {
        return isCut;
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public boolean isLongImage() {
        return isLongImage;
    }

    public static Creator<LocalMedia> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<LocalMedia> CREATOR = new Creator<LocalMedia>() {
        @Override
        public LocalMedia createFromParcel(Parcel in) {
            return new LocalMedia(in);
        }

        @Override
        public LocalMedia[] newArray(int size) {
            return new LocalMedia[size];
        }
    };

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(path);
        dest.writeLong(duration);
        dest.writeString(mimeType);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeLong(size);
        dest.writeString(fileName);
        dest.writeString(parentFolderName);
        dest.writeByte((byte) (isCut ? 1 : 0));
        dest.writeByte((byte) (isCompressed ? 1 : 0));
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeByte((byte) (isOriginal ? 1 : 0));
        dest.writeByte((byte) (isLongImage ? 1 : 0));
    }

    public LocalMedia(Parcel in) {
        id = in.readLong();
        path = in.readString();
        duration = in.readLong();
        mimeType = in.readString();
        width = in.readInt();
        height = in.readInt();
        size = in.readLong();
        fileName = in.readString();
        parentFolderName = in.readString();
        isCut = in.readByte() != 0;
        isCompressed = in.readByte() != 0;
        isChecked = in.readByte() != 0;
        isOriginal = in.readByte() != 0;
        isLongImage = in.readByte() != 0;
    }
}
