package br.com.up.carrosup.fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.parceler.Parcels;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import br.com.up.carrosup.R;
import br.com.up.carrosup.activity.CarroActivity;
import br.com.up.carrosup.domain.Carro;
import br.com.up.carrosup.domain.CarroService;
import br.com.up.carrosup.fragment.adapter.CarroAdapter;
import br.com.up.carrosup.utils.BroadcastUtil;
import livroandroid.lib.fragment.BaseFragment;
import livroandroid.lib.utils.AndroidUtils;

/**
 * Created by ricardo on 12/06/15.
 */
public class CarrosFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<Carro> carros;
    private String tipo;
    private SwipeRefreshLayout swipeLayout;
    // Action Bar de Contexto
    private ActionMode actionMode;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            String action = intent.getAction();
            if(BroadcastUtil.ACTION_CARRO_SALVO.equals(action)) {
                // Ao receber o broadcast, recarrega a lista.
                listaCarros(false);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.tipo = getArguments().getString("tipo");
        }
        // Registra receiver para receber broadcasts
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(BroadcastUtil.ACTION_CARRO_SALVO));
        // Para inflar itens de menu na toolbar
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cancela o recebimento de mensagens
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carros, container, false);
        // Lista
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        // FAB
        view.findViewById(R.id.fabAddCarro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adicionar um carro
                Intent intent = new Intent(getActivity(), CarroActivity.class);
                intent.putExtra("editMode", true);
                ActivityCompat.startActivity(getActivity(), intent, null);
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listaCarros(false);
    }
    private void listaCarros(final boolean refresh) {
        startTask("carros", new BaseTask<Object>() {
            @Override
            public Object execute() throws Exception {
                // Código em background aqui
                carros = CarroService.getCarros(getContext(), tipo);
                return null;
            }

            @Override
            public void updateView(Object response) {
                super.updateView(response);
                // O código que atualiza a interface precisa executar na UI Thread
                recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
            }
        }, R.id.progress);
    }
    protected CarroAdapter.CarroOnClickListener onClickCarro() {
        return new CarroAdapter.CarroOnClickListener() {
            @Override
            public void onClickCarro(CarroAdapter.CarrosViewHolder holder, int idx) {
                Carro c = carros.get(idx);
                if (actionMode == null) {
                    // Troca de tela (detalhes do carro).
                    Intent intent = new Intent(getContext(), CarroActivity.class);
                    intent.putExtra("carro", Parcels.wrap(c));
                    // Transição animada
                    ImageView img = (ImageView) holder.img;
                    String key = getString(R.string.transition_key);
                    ActivityOptionsCompat opts = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity(), img, key);
                    ActivityCompat.startActivity(getActivity(), intent, opts.toBundle());
                } else {
                    // Seleciona o carro e atualiza a lista
                    c.selected = !c.selected;
                    updateActionModeTitle();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
            @Override
            public void onLongClickCarro(CarroAdapter.CarrosViewHolder holder, int idx) {
                //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                actionMode = getAppCompatActivity().startSupportActionMode(getActionModeCallback());
                Carro c = carros.get(idx);
                c.selected = true;
                recyclerView.getAdapter().notifyDataSetChanged();
                updateActionModeTitle();
            }
        };
    }
    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listaCarros(true);
                swipeLayout.setRefreshing(false);
            }
        };
    }
    private ActionMode.Callback getActionModeCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.menu_frag_carros_context, menu);
                return true;
            }
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_remove) {
                    deletarCarrosSelecionados();
                }
                // Encerra o action mode
                mode.finish();
                return true;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Limpa o ActionMode e carros selecionados
                actionMode = null;
                for (Carro c : carros) {
                    c.selected = false;
                }
                // Atualiza a lista
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        };
    }
    // Deletar carros selecionados ao abrir a CAB
    private void deletarCarrosSelecionados() {
        final List<Carro> selectedCarros = getSelectedCarros();
        if(selectedCarros.size() > 0) {
            startTask("deletar",new BaseTask(){
                @Override
                public Object execute() throws Exception {
                    boolean ok = CarroService.delete(getContext(), selectedCarros);
                    if(ok) {
                        // Se excluiu do banco, remove da lista da tela.
                        for (Carro c : selectedCarros) {
                            carros.remove(c);
                        }
                    }
                    return null;
                }
                @Override
                public void updateView(Object count) {
                    super.updateView(count);
                    // Mostra mensagem de sucesso
                    snack(recyclerView, selectedCarros.size() + " carros excluídos com sucesso");
                    // Busca novamente no web service
                    //listaCarros(true);
                    // Atualiza o adapter da lista (faz usar o novo array)
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            });
        }
    }
    private List<Carro> getSelectedCarros() {
        List<Carro> list = new ArrayList<Carro>();
        for (Carro c : carros) {
            if (c.selected) {
                list.add(c);
            }
        }
        return list;
    }

    private void updateActionModeTitle() {
        if (actionMode != null) {
            actionMode.setTitle("Selecione os carros.");
            actionMode.setSubtitle(null);
            List<Carro> selectedCarros = getSelectedCarros();
            if (selectedCarros.size() == 1) {
                actionMode.setSubtitle("1 carro selecionado");
            } else if (selectedCarros.size() > 1) {
                actionMode.setSubtitle(selectedCarros.size() + " carros selecionados");
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag_carros, menu);
        // SearchView
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(onSearch());
    }
    private SearchView.OnQueryTextListener onSearch() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscaCarros(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }
    private void buscaCarros(final String nome) {
        // Atualiza ao fazer o gesto Swipe To Refresh
        if (AndroidUtils.isNetworkAvailable(getContext())) {
            startTask("carros", new BaseTask<Object>() {
                @Override
                public Object execute() throws Exception {
                    // Código em background aqui
                    carros = CarroService.seachByNome(getContext(), nome);
                    return null;
                }
                @Override
                public void updateView(Object response) {
                    super.updateView(response);
// O código que atualiza a interface precisa executar na UI Thread
                    recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
                }
            }, R.id.progress);
        } else {
            alert(R.string.msg_error_conexao_indisponivel);
        }
    }
}