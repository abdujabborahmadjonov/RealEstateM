package uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.DialogConnectionBinding

class ConnectionDialog(val context: Context, private val connectionDialogClicked : ConnectionDialogClicked) {

    private var connectionDialog : AlertDialog? = null
    private lateinit var connectionBinding : DialogConnectionBinding
    
    interface ConnectionDialogClicked{
        fun connectDialogRefreshClicked(refreshType:String)
        fun connectDialogBackClicked(refreshType:String)
    }

    private fun createDialog() {
        connectionDialog = AlertDialog.Builder(context).create()
        connectionBinding = DialogConnectionBinding.inflate(LayoutInflater.from(context))
        connectionDialog?.setView(connectionBinding.root)
        connectionDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        connectionDialog?.setCancelable(false)
        connectionDialog?.show()
    }
    
    fun showDialog(refreshType: String, animationType: String, message: String) {
        dismissDialog()
        if (connectionDialog == null) {
            createDialog()
            animationChanger(animationType, message)
            connectionBinding.dgConnectionRetryBtn.setOnClickListener {
                connectionDialogClicked.connectDialogRefreshClicked(refreshType)
            }
            connectionBinding.dgConectionDismissBtn.setOnClickListener {
                connectionDialogClicked.connectDialogBackClicked(refreshType)
                dismissDialog()
            }
        }
    }

    fun animationChanger(key: String, message: String) {

        if (connectionDialog == null) {
            createDialog()
        }

        when(key) {
            Constants.NO_INTERNET -> {
                connectionBinding.dgConnectionText.text = "Tarmoq bilan aloqa mavjud emas!"
                connectionBinding.dgConnectionBtnLayout.visibility = View.VISIBLE
            }
            Constants.IS_LOADING -> {
                connectionBinding.dgConnectionText.text = message
                connectionBinding.dgConnectionBtnLayout.visibility = View.GONE
            }
            Constants.IS_CHECK_API -> {

                connectionBinding.dgConnectionText.text = message
                connectionBinding.dgConnectionBtnLayout.visibility = View.GONE
                object : CountDownTimer(3000,3000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        dismissDialog()
                    }
                }.start()
            }
            Constants.IS_NOT_CHECKED -> {

                connectionBinding.dgConnectionText.text = message
                connectionBinding.dgConnectionBtnLayout.visibility = View.VISIBLE
            }
        }
    }

    fun dismissDialog() {
        connectionDialog?.dismiss()
        connectionDialog = null
    }
}