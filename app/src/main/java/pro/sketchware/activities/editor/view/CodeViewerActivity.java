package pro.sketchware.activities.editor.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.besome.sketch.lib.base.BaseAppCompatActivity;

import a.a.a.Lx;
import mod.hey.studios.util.Helper;
import pro.sketchware.R;
import pro.sketchware.databinding.ActivityCodeViewerBinding;
import pro.sketchware.utility.EditorUtils;
import pro.sketchware.utility.UI;

public class CodeViewerActivity extends BaseAppCompatActivity {

    public static final String SCHEME_XML = "xml";
    public static final String SCHEME_JAVA = "java";
    /** Boolean extra: makes the editor editable and shows a Save button that returns the code in {@link #EXTRA_CODE}. */
    public static final String EXTRA_EDITABLE = "editable";
    public static final String EXTRA_CODE = "code";

    private ActivityCodeViewerBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);

        binding = ActivityCodeViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        var code = getIntent().getStringExtra("code");
        var scheme = getIntent().getStringExtra("scheme");
        var scId = getIntent().getStringExtra("sc_id");

        binding.toolbar.setNavigationOnClickListener(Helper.getBackPressedClickListener(this));
        binding.toolbar.setSubtitle(scId);

        binding.editor.setTypefaceText(EditorUtils.getTypeface(this));
        binding.editor.setTextSize(14);
        binding.editor.setText(Lx.j(code, false));
        boolean editable = getIntent().getBooleanExtra(EXTRA_EDITABLE, false);
        binding.editor.setEditable(editable);
        binding.editor.setWordwrap(false);
        if (editable) {
            binding.toolbar.inflateMenu(R.menu.menu_code_viewer_save);
            binding.toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_save_code) {
                    Intent result = new Intent();
                    result.putExtra(EXTRA_CODE, binding.editor.getText().toString());
                    setResult(RESULT_OK, result);
                    finish();
                    return true;
                }
                return false;
            });
        }
        loadColorScheme(scheme);

        UI.addSystemWindowInsetToPadding(binding.appBarLayout, true, true, true, false);
        UI.addSystemWindowInsetToMargin(binding.editor, true, false, true, true);
    }

    private void loadColorScheme(String scheme) {
        if (scheme.equals(SCHEME_XML)) {
            EditorUtils.loadXmlConfig(binding.editor);
        } else {
            EditorUtils.loadJavaConfig(binding.editor);
        }
    }
}