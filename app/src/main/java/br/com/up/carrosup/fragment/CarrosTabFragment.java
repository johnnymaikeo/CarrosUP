package br.com.up.carrosup.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.up.carrosup.R;
import br.com.up.carrosup.fragment.adapter.TabsAdapter;

/**
 * Fragment que controla as Tabs dos carros (classicos,esportivos,luxo)
 */
public class CarrosTabFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carros_tab, container, false);

        // ViewPager
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        // Adapter do ViewPager
        viewPager.setAdapter(new TabsAdapter(getContext(), getChildFragmentManager()));

        // Cria as tabs com base no conteúdo do ViewPager
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(R.color.branco, R.color.azul);

        return view;
    }
}