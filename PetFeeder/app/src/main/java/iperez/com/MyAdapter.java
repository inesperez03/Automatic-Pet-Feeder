package iperez.com;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//Dos clases creadas para enlistar los animales cargados desde el Firebase

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override

    //Creación del ViewHolder usando como base el layout llamado recycler_irem

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    //Proceso progresivo de creación de la lista de animales, usando el holder definido anteriormente

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recName.setText(dataList.get(position).getDataName());
        holder.recAnimal.setText(dataList.get(position).getDataAnimal());
        holder.recWeight.setText(dataList.get(position).getDataWeight() + " kg");

        //Cada vez que pulses sobre uno de los animales, te abrirá DetailActivity con sus datos.

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("name", dataList.get(holder.getAdapterPosition()).getDataName());
                intent.putExtra("animal", dataList.get(holder.getAdapterPosition()).getDataAnimal());
                intent.putExtra("weight", dataList.get(holder.getAdapterPosition()).getDataWeight());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("months", dataList.get(holder.getAdapterPosition()).getDataMonths());
                intent.putExtra("years", dataList.get(holder.getAdapterPosition()).getDataYears());
                intent.putExtra("food", dataList.get(holder.getAdapterPosition()).getDataFood());
                intent.putExtra("water", dataList.get(holder.getAdapterPosition()).getDataWater());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

//Creación del holder.

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recName, recAnimal, recWeight, recYears, recMonths;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recName = itemView.findViewById(R.id.recName);
        recAnimal = itemView.findViewById(R.id.recAnimal);
        recWeight = itemView.findViewById(R.id.recWeight);
    }
}
