import java.util.ArrayList;
import java.util.List;

public class Executor {
    public List<Integer> value = new ArrayList<Integer>();
    public String type;
    public List<Executor> subValue = new ArrayList<Executor>();
    public Executor parent;
    public int id;
    private static int count = 0;

    public Executor() {
        id = ++count;
    }

    public List<Integer> execute() {
        List<Integer> resultList = new ArrayList<Integer>();

        if (subValue.size() != 0) {
            for (Executor childValue : subValue) {
                value.addAll(childValue.execute());
            }
        }

        if (value.size() == 0) return new ArrayList<>();
        else if (type == null) return value;

        boolean result = type.equals("and") ? true : false;

        for (int i = 0; i < value.size(); i++) {
            if (type.equals("and") && value.get(i) != 1) {
                result = false;
                break;
            } else if (type.equals("or") && value.get(i) == 1) {
                result = true;
                break;
            } else if (type.equals("not")) {
                result = value.get(i) == 0;
                break;
            }
        }
        resultList.add(result ? 1 : 0);
        return resultList;
    }

    public void setValue(Integer v) {
        value.add(v);
    }

    @Override
    public String toString() {
        return "Id: " + id + " Value:" + value.toString() + " Type:" + type + "\n\tSub:" + (subValue != null ? subValue.toString() : "");
    }
}