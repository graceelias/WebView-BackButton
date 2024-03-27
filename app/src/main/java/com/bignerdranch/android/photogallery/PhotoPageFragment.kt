package com.bignerdranch.android.photogallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoPageBinding

class PhotoPageFragment : Fragment() {
    private val args: PhotoPageFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPhotoPageBinding.inflate(
            inflater,
            container,
            false
        )

        binding.apply {
            progressBar.max = 100

            webView.apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(args.photoPageUri.toString())

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(
                        webView: WebView,
                        newProgress: Int
                    ) {
                        if (newProgress == 100) {
                            progressBar.visibility = View.GONE
                        } else {
                            progressBar.visibility = View.VISIBLE
                            progressBar.progress = newProgress
                        }
                    }

                    override fun onReceivedTitle(
                        view: WebView?,
                        title: String?
                    ) {
                        val parent = requireActivity() as AppCompatActivity
                        parent.supportActionBar?.subtitle = title
                    }
                }
            }

            webView.setOnKeyListener{ v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN)
                {
                    val webView = v as WebView

                    when (keyCode)
                    {
                        KeyEvent.KEYCODE_BACK -> {
                            if (webView.canGoBack())
                            {
                                webView.goBack()
                                true
                            }
                            else
                            {
                                activity?.onBackPressed()
                                true
                            }
                        }
                        else -> false
                    }
                }
                else
                {
                    false
                }
            }
        }

        return binding.root
    }
}
