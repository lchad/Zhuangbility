package com.liuchad.zhuangbility.view;

import com.liuchad.zhuangbility.base.IBaseView;
import com.liuchad.zhuangbility.vo.RemoteImage;

import java.util.List;

/**
 * Created by lchad on 2017/3/21.
 * Github: https://github.com/lchad
 */

public interface IElementaryView extends IBaseView {
    void setData(List<RemoteImage> value);

    void setRefresh(boolean b);
}
