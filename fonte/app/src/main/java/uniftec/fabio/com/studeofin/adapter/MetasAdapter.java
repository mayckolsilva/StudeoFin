package uniftec.fabio.com.studeofin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.vo.MetasVO;

public class MetasAdapter extends ArrayAdapter<MetasVO> {


    private final Context context;
    private final ArrayList<MetasVO> metas;

    public MetasAdapter(Context context,  ArrayList<MetasVO> metas) {

        super(context, R.layout.item_metas, metas);
        this.context = context;
        this.metas = metas;

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent){

        try {
            View view;
            ViewHolder3 holder;
            if (convertView == null) {
                view = LayoutInflater.from(context)
                        .inflate(R.layout.item_lancamentos, parent, false);
                holder = new ViewHolder3(view);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder3) view.getTag();
            }

            final MetasVO obj = metas.get(position);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String sData = sdf.format(metas.get(position).getDtaMeta());

            holder.getT1().setText(metas.get(position).getDesMeta());
            holder.getT2().setText(String.format("%.2f",metas.get(position).getVlrMeta()));
            holder.getT3().setText(sData);
            holder.getT4().setText(String.format("%.2f",metas.get(position).getVlrMetaAtingida()));

            return view;
        } catch (Exception e) {
            Toast.makeText(context, "Erro ao importar lancamentos", Toast.LENGTH_LONG).show();
            return convertView;

        }
    }
}

class ViewHolder3 {
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;

    public ViewHolder3(View view) {
        t1 = (TextView) view.findViewById(R.id.des_meta);
        t2 = (TextView) view.findViewById(R.id.edt_vlrMeta);
        t3 = (TextView) view.findViewById(R.id.edt_dtaMeta);
        t4 = (TextView) view.findViewById(R.id.vlr_atingido_meta);


    }

    public TextView getT1() { return t1; }

    public void setT1(TextView t1) { this.t1 = t1; }

    public TextView getT2() { return t2; }

    public void setT2(TextView t2) { this.t2 = t2; }

    public TextView getT3() { return t3; }

    public void setT3(TextView t3) { this.t3 = t3; }

    public TextView getT4() { return t4; }

    public void setT4(TextView t4) { this.t4 = t4; }
}
