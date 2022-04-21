package uniftec.fabio.com.studeofin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.vo.LancamentosVO;

public class LancamentosAdapter extends ArrayAdapter<LancamentosVO> {

    private final Context context;
    private final ArrayList<LancamentosVO> lancamentos;

    public LancamentosAdapter(Context context,  ArrayList<LancamentosVO> lancamentos) {

        super(context, R.layout.item_lancamentos, lancamentos);
        this.context = context;
        this.lancamentos = lancamentos;


    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent){

        try {
            View view;
            ViewHolder2 holder;
            if (convertView == null) {
                view = LayoutInflater.from(context)
                        .inflate(R.layout.item_lancamentos, parent, false);
                holder = new ViewHolder2(view);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder2) view.getTag();
            }

            final LancamentosVO obj = lancamentos.get(position);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String sData = sdf.format(lancamentos.get(position).getDtaLancamento());
            holder.getT1().setText(sData);

            holder.getT2().setText(lancamentos.get(position).getDesLancamento());
            holder.getT3().setText(lancamentos.get(position).getDesCategoria());
            holder.getT4().setText(String.format("%.2f",lancamentos.get(position).getVlrLancamento()));

            return view;
        } catch (Exception e) {
            Toast.makeText(context, "Erro ao importar lancamentos", Toast.LENGTH_LONG).show();
            return convertView;

        }
    }
}

class ViewHolder2 {
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;

    public ViewHolder2(View view) {
        t1 = (TextView) view.findViewById(R.id.des_data);
        t2 = (TextView) view.findViewById(R.id.des_lancamento);
        t3 = (TextView) view.findViewById(R.id.des_lancamento_categoria);
        t4 = (TextView) view.findViewById(R.id.des_valor);


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
