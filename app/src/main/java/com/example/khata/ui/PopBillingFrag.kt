package com.example.khata.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.khata.R
import com.example.khata.pojo.CustomerHolder
import com.example.khata.pojo.toast
import com.example.khata.pojo.validOneMore
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.pop_billing.view.*

class PopBillingFrag: BottomSheetDialogFragment(), View.OnClickListener {

    val TAG = "ActionBottomDialog"
    private lateinit var mcontext: Context
    private lateinit var layView: View

    var flag: Boolean = false
    var max: Float = 0f
    var link: String = ""

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.share_text -> {
                val uri = Uri.parse("smsto:+91${CustomerHolder.customer.mobile}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", link)
                startActivity(intent)
            }

            R.id.share_whats -> {
                sendWhatsapp()
            }

            R.id.btn_copy -> {
                val clipboardManager = mcontext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", link)
                clipboardManager.setPrimaryClip(clipData)
                toast(context!!,"Link Copied")
            }

            R.id.btn_editam -> {
                if (flag) {
                    layView.ly_error.error = null

                    val er = validOneMore(layView.edt_am.text.toString())
                    when {
                        er!=true -> layView.ly_error.error = er.toString()
                        layView.edt_am.text.toString().toFloat()>max -> layView.ly_error.error = "Max limit exceeded"
                        else -> {
                            layView.chip_link.text = generateLink(layView.edt_am.text.toString())
                            layView.btn_editam.text = "Edit"
                            layView.edt_am.isClickable = false
                            layView.edt_am.isEnabled = false
                            flag = false
                        }
                    }
                } else {
                    layView.btn_editam.text = "Save"
                    layView.edt_am.isClickable = true
                    layView.edt_am.isEnabled = true
                    flag = true
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        mcontext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layView = inflater.inflate(R.layout.pop_billing, container, false)
        return layView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layView.btn_editam.setOnClickListener(this)
        layView.share_text.setOnClickListener(this)
        layView.share_whats.setOnClickListener(this)
        layView.btn_copy.setOnClickListener(this)

        max = CustomerHolder.customer.rs
        layView.txt_limit.text = max.toString()
        layView.edt_am.setText(max.toString())

        link = generateLink(max.toString())
        layView.chip_link.text = link

        super.onViewCreated(view, savedInstanceState)
    }

    private fun generateLink(str: String): String {
        return "https://xyz.pay/$str"
    }

    private fun sendWhatsapp() {
        try {
            mcontext.packageManager?.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, link)
            intent.setPackage("com.whatsapp")
            intent.type = "text/plain"
            startActivity(intent)
        } catch (e: PackageManager.NameNotFoundException) {
            val uri = Uri.parse("market://details?id=com.whatsapp")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            startActivity(goToMarket)
        }
    }
}