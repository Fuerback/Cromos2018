package com.fuerback.desenvolvimento.cromos.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuerback.desenvolvimento.cromos.R;
import com.fuerback.desenvolvimento.cromos.entity.Cromo;

import java.util.List;

public class CromoAdapter  extends RecyclerView.Adapter<CromoAdapter.MyViewHolder> {

    private List<Cromo> listaCromos;

    public CromoAdapter(List<Cromo> lista) {
        listaCromos = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemCromo = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.lista_cromo_adapter, parent, false);
        return new MyViewHolder(itemCromo);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cromo cromo = listaCromos.get(position);
        holder.numero.setText( Integer.toString(cromo.getNumero()) );
        holder.nome.setText( cromo.getNome() );

        if(cromo.getNumero() >= 0 && cromo.getNumero() <= 7){
            holder.flag.setImageResource(R.drawable.ic_introducao_24dp);
        } else if(cromo.getNumero() >= 8 && cromo.getNumero() <= 19){
            holder.flag.setImageResource(R.drawable.ic_estadios_24dp);
        } else if (cromo.getNumero() >= 20 && cromo.getNumero() <= 31){
            holder.flag.setImageResource(R.drawable.ic_cidades_24dp);
        } else if (cromo.getNumero() >= 32 && cromo.getNumero() <= 51){
            holder.flag.setImageResource(R.drawable.ic_russia);
        } else if(cromo.getNumero() >= 52 && cromo.getNumero() <= 71){
            holder.flag.setImageResource(R.drawable.ic_arabia);
        } else if(cromo.getNumero() >= 72 && cromo.getNumero() <= 91){
            holder.flag.setImageResource(R.drawable.ic_egito);
        } else if(cromo.getNumero() >= 92 && cromo.getNumero() <= 111){
            holder.flag.setImageResource(R.drawable.ic_uruguai);
        } else if(cromo.getNumero() >= 112 && cromo.getNumero() <= 131){
            holder.flag.setImageResource(R.drawable.ic_portugal);
        }else if(cromo.getNumero() >= 132 && cromo.getNumero() <= 151){
            holder.flag.setImageResource(R.drawable.ic_espanha);
        }else if(cromo.getNumero() >= 152 && cromo.getNumero() <= 171){
            holder.flag.setImageResource(R.drawable.ic_marrocos);
        }else if(cromo.getNumero() >= 172 && cromo.getNumero() <= 191){
            holder.flag.setImageResource(R.drawable.ic_ira);
        }else if(cromo.getNumero() >= 192 && cromo.getNumero() <= 211){
            holder.flag.setImageResource(R.drawable.ic_franca);
        }else if(cromo.getNumero() >= 212 && cromo.getNumero() <= 231){
            holder.flag.setImageResource(R.drawable.ic_australia_new);
        }else if(cromo.getNumero() >= 232 && cromo.getNumero() <= 251){
            holder.flag.setImageResource(R.drawable.ic_peru);
        }else if(cromo.getNumero() >= 252 && cromo.getNumero() <= 271){
            holder.flag.setImageResource(R.drawable.ic_dinamarca);
        }else if(cromo.getNumero() >= 272 && cromo.getNumero() <= 291){
            holder.flag.setImageResource(R.drawable.ic_argentina);
        }else if(cromo.getNumero() >= 292 && cromo.getNumero() <= 311){
            holder.flag.setImageResource(R.drawable.ic_islandia);
        }else if(cromo.getNumero() >= 312 && cromo.getNumero() <= 331){
            holder.flag.setImageResource(R.drawable.ic_croacia);
        }else if(cromo.getNumero() >= 332 && cromo.getNumero() <= 351){
            holder.flag.setImageResource(R.drawable.ic_nigeria);
        }else if(cromo.getNumero() >= 352 && cromo.getNumero() <= 371){
            holder.flag.setImageResource(R.drawable.ic_brasil_flag);
        }else if(cromo.getNumero() >= 372 && cromo.getNumero() <= 391){
            holder.flag.setImageResource(R.drawable.ic_suica_24dp);
        }else if(cromo.getNumero() >= 392 && cromo.getNumero() <= 411){
            holder.flag.setImageResource(R.drawable.ic_costa_rica);
        }else if(cromo.getNumero() >= 412 && cromo.getNumero() <= 431){
            holder.flag.setImageResource(R.drawable.ic_servia);
        }else if(cromo.getNumero() >= 432 && cromo.getNumero() <= 451){
            holder.flag.setImageResource(R.drawable.ic_alemanha);
        }else if(cromo.getNumero() >= 452 && cromo.getNumero() <= 471){
            holder.flag.setImageResource(R.drawable.ic_mexico);
        }else if(cromo.getNumero() >= 472 && cromo.getNumero() <= 491){
            holder.flag.setImageResource(R.drawable.ic_suecia);
        }else if(cromo.getNumero() >= 492 && cromo.getNumero() <= 511){
            holder.flag.setImageResource(R.drawable.ic_korea);
        }else if(cromo.getNumero() >= 512 && cromo.getNumero() <= 531){
            holder.flag.setImageResource(R.drawable.ic_belgica);
        }else if(cromo.getNumero() >= 532 && cromo.getNumero() <= 551){
            holder.flag.setImageResource(R.drawable.ic_panama);
        }else if(cromo.getNumero() >= 552 && cromo.getNumero() <= 571){
            holder.flag.setImageResource(R.drawable.ic_tunisia);
        }else if(cromo.getNumero() >= 572 && cromo.getNumero() <= 591){
            holder.flag.setImageResource(R.drawable.ic_inglaterra);
        }else if(cromo.getNumero() >= 592 && cromo.getNumero() <= 611){
            holder.flag.setImageResource(R.drawable.ic_polonia);
        }else if(cromo.getNumero() >= 612 && cromo.getNumero() <= 631){
            holder.flag.setImageResource(R.drawable.ic_senegal);
        }else if(cromo.getNumero() >= 632 && cromo.getNumero() <= 651){
            holder.flag.setImageResource(R.drawable.ic_colombia);
        }else if(cromo.getNumero() >= 652 && cromo.getNumero() <= 671){
            holder.flag.setImageResource(R.drawable.ic_japao);
        }else if(cromo.getNumero() >= 672 && cromo.getNumero() <= 681){
            holder.flag.setImageResource(R.drawable.ic_lendas_24dp);
        }


        if(cromo.isPossui()){
            if(cromo.isRepetida()){
                holder.imagem.setImageResource(R.drawable.ic_done_all_blue_24dp);
            }
            else{
                holder.imagem.setImageResource(R.drawable.ic_done_green_24dp);
            }
        }
        else{
            holder.imagem.setImageResource(R.drawable.ic_close_red_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return this.listaCromos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView numero;
        TextView nome;
        ImageView imagem;
        ImageView flag;

        public MyViewHolder(View itemView) {
            super(itemView);

            imagem = itemView.findViewById(R.id.imagemAdapter);
            flag = itemView.findViewById(R.id.imageFlag);
            numero = itemView.findViewById(R.id.numeroAdapter);
            nome = itemView.findViewById(R.id.nomeAdapter);
        }
    }
}
