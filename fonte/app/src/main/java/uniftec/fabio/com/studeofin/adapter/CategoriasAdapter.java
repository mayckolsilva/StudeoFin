package uniftec.fabio.com.studeofin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import uniftec.fabio.com.studeofin.R;
import uniftec.fabio.com.studeofin.databinding.FragmentCategoriasBinding;
import uniftec.fabio.com.studeofin.databinding.FragmentPerfilBinding;
import uniftec.fabio.com.studeofin.fragment.CategoriasFragment;
import uniftec.fabio.com.studeofin.vo.CategoriasVO;

public class CategoriasAdapter extends ArrayAdapter<CategoriasVO> {

    private final Context context;
    private final ArrayList<CategoriasVO> categorias;

    public CategoriasAdapter(Context context, ArrayList<CategoriasVO> categorias) {
        super(context, R.layout.item_categorias, categorias);

        this.context = context;
        this.categorias = categorias;

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        try {
            View view;
            ViewHolder holder;

            if(convertView==null){
                view = LayoutInflater.from(context)
                        .inflate(R.layout.item_categorias,parent,false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                view=convertView;
                holder = (ViewHolder) view.getTag();
            }

            final CategoriasVO obj = categorias.get(position);

            holder.getT1().setText(categorias.get(position).getDesCategoria());

            if(categorias.get(position).getIndTipo()==0){
                holder.getT2().setText("Receita");
            } else if (categorias.get(position).getIndTipo()==1) {
                holder.getT2().setText("Despesa");
            } else {
                holder.getT2().setText("Meta");
            }
            return view;
        } catch (Exception e){
            Toast.makeText(context,"Erro ao importar categorias", Toast.LENGTH_LONG).show();
            return convertView;
        }
    }
}

class ViewHolder {
    TextView t1;
    TextView t2;

    public ViewHolder(View view) {
        t1 = (TextView) view.findViewById(R.id.des_categoria);
        t2 = (TextView) view.findViewById(R.id.des_tipo);

    }

    public TextView getT1() { return t1; }

    public void setT1(TextView t1) { this.t1 = t1; }

    public TextView getT2() { return t2; }

    public void setT2(TextView t2) { this.t2 = t2; }

}