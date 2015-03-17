package com.leo.game.number;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ConfirmDialog extends DialogFragment {
	TextView txtTitle;
	ImageView iconTitle;
	String title;
	int resIcon;
	Button btnConfirm;
	Button btnCancle;
	OnDialogClickListener onDialogClickListener;

	public static interface OnDialogClickListener {
		public void onClickConfirm();

		public void onClickCancle();
	}

	public void setOnDialigClick(OnDialogClickListener onDialogClick) {
		this.onDialogClickListener = onDialogClick;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.dialogfragment_confirm, null);
		btnConfirm = (Button) view.findViewById(R.id.DialogFragment_btnOK);
		btnCancle = (Button) view.findViewById(R.id.DialogFragment_btnCancle);
		if (onDialogClickListener != null) {
			btnCancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onDialogClickListener.onClickCancle();
				}
			});
			btnConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onDialogClickListener.onClickConfirm();
				}
			});
		}
		initTitle(view);
		return view;
	}

	public void setTitle(int resIcon, String title) {
		this.title = title;
		this.resIcon = resIcon;
	}

	protected void initTitle(View view) {
		txtTitle = (TextView) view.findViewById(R.id.Dialog_basic_title_txt);
		iconTitle = (ImageView) view.findViewById(R.id.Dialog_basic_title_Icon);
		if (txtTitle != null) {
			txtTitle.setText(title);
		}
		try {
			if (resIcon == 0) {
				iconTitle.setVisibility(View.GONE);
			} else {
				iconTitle.setImageResource(resIcon);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
