package com.liuchad.zhuangbility.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.adapter.LicenceAdapter;
import com.liuchad.zhuangbility.util.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lchad on 2017/3/4.
 */

public class LicenceViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.detail)
    TextView mDetail;
    @Bind(R.id.card_view)
    CardView mCardView;

    private LicenceAdapter.LicenceModel mLicenceModel;

    public LicenceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLicenceModel != null) {
                    CommonUtils.gotoUrl(view.getContext(), mLicenceModel.url);
                }
            }
        });
    }

    public void bind(LicenceAdapter.LicenceModel licenceModel) {
        if (licenceModel == null) {
            return;
        }
        mLicenceModel = licenceModel;
        mTitle.setText(licenceModel.name);
        mDetail.setText(licenceModel.licence);
    }
}
