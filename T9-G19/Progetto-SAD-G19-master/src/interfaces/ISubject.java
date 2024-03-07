package interfaces;
import java.util.List;
public interface ISubject {
        void registerObserver(IObserver observer);
        void unregisterObserver(IObserver observer);
        void notifyObservers();
}
