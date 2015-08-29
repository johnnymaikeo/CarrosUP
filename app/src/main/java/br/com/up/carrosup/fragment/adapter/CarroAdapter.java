package br.com.up.carrosup.fragment.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.up.carrosup.R;
import br.com.up.carrosup.domain.Carro;

public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.CarrosViewHolder> {
    private final List<Carro> carros;
    private final Context context;
    private final CarroOnClickListener onClickListener;

    public interface CarroOnClickListener {
        public void onClickCarro(CarrosViewHolder holder, int idx);

        public void onLongClickCarro(CarrosViewHolder holder, int idx);
    }

    public CarroAdapter(Context context, List<Carro> carros, CarroOnClickListener onClickListener) {
        this.context = context;
        this.carros = carros;
        this.onClickListener = onClickListener;
    }

    @Override
    public CarrosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Este método cria uma subclasse de RecyclerView.ViewHolder
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_carro, viewGroup, false);
        // Cria a classe do ViewHolder
        CarrosViewHolder holder = new CarrosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CarrosViewHolder holder, final int position) {
        // Carro da linha
        Carro c = carros.get(position);

        // Atualizada os valores nas views
        holder.tNome.setText(c.nome);
        holder.tDesc.setText(c.desc);

        // Foto do carro
        Picasso.with(context).load(c.urlFoto).into(holder.img);

        // Click
        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chama o listener para informar que clicou no Carro
                    onClickListener.onClickCarro(holder, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Chama o listener para informar que clicou no Carro
                    onClickListener.onLongClickCarro(holder, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.carros != null ? this.carros.size() : 0;
    }

    // Subclasse de RecyclerView.ViewHolder. Contém todas as views.
    public static class CarrosViewHolder extends RecyclerView.ViewHolder {
        public TextView tNome;
        public TextView tDesc;
        public ImageView img;
        public CardView cardView;

        public CarrosViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.tNome);
            tDesc = (TextView) view.findViewById(R.id.tDesc);
            img = (ImageView) view.findViewById(R.id.img);
            ViewCompat.setTransitionName(img,view.getContext().getString(R.string.transition_key));
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}