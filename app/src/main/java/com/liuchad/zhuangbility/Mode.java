package com.liuchad.zhuangbility;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2016/10/15.
 */

@IntDef({Mode.MODE_SINGLE, Mode.MODE_MULTI})
@Retention(RetentionPolicy.CLASS)
public @interface Mode {
  /**
   * 单选
   */
  int MODE_SINGLE = 0;
  /**
   * 多选
   */
  int MODE_MULTI = 1;

}