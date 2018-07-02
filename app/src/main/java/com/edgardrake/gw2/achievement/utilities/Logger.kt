package com.edgardrake.gw2.achievement.utilities

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.library.BaseApplication
import kotlinx.android.synthetic.main.logger_dialog.view.*
import kotlinx.android.synthetic.main.logger_entry.view.*

class Logger(val context: Context) {

    private val entries = ArrayList<Pair<String, String>>()

    fun addEntry(key: String?, value: String?) : Logger {
        entries.add(Pair(key ?: "", value ?: ""))
        return this
    }

    fun show() {
        val mDialog = LayoutInflater.from(context).inflate(R.layout.logger_dialog, null, false)
        mDialog.logDataset.adapter = LoggerAdapter(entries)
        mDialog.logDataset.setHasFixedSize(true)

        AlertDialog.Builder(context)
            .setView(mDialog)
            .setPositiveButton(android.R.string.ok, { dialog, _ -> dialog.dismiss() })
            .create()
            .show()
    }

    class LoggerHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.logger_entry, parent, false)) {

        var key: String
            get() = itemView.logKey.text.toString()
            set(value) { itemView.logKey.text = value }

        var value: String
            get() = itemView.logValue.text.toString()
            set(newVal) { itemView.logValue.text = newVal }
    }

    class LoggerAdapter(val dataset: List<Pair<String, String>>):
        RecyclerView.Adapter<LoggerHolder>() {

        override fun getItemCount(): Int {
            return dataset.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoggerHolder {
            return LoggerHolder(parent)
        }

        override fun onBindViewHolder(holder: LoggerHolder, position: Int) {
            holder.key = dataset[holder.adapterPosition].first
            holder.value = dataset[holder.adapterPosition].second
        }
    }
}